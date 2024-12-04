package com.example.Chasse.Activities.Game.Chat;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import com.example.Chasse.Model.Message;
import com.example.Chasse.Model.SQLite.MessageDatabase;
import com.example.Chasse.Model.SocketManager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ChatService extends Service {

    private final IBinder binder = new LocalBinder();
    private final Socket socket = SocketManager.getInstance().getSocket();
    private MessageDatabase dbMessages;
    private ExecutorService executorService;

    @Override
    public void onCreate() {
        super.onCreate();
        dbMessages = MessageDatabase.getInstance(this);
        executorService = Executors.newSingleThreadExecutor();
        clearMessagesInBackground();
        initSocketListeners();


        Intent intent = new Intent("com.example.Chasse.SERVICE_STATUS");
        intent.putExtra("isServiceRunning", true);
        sendBroadcast(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    private void initSocketListeners() {
        socket.on("receive message", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                String messageContent = (String) args[0];
                Log.d("ChatService", "Message reçu du serveur: " + messageContent);

                Message message = new Message(messageContent, true);
                addMessageInBackground(message);

                Intent broadcastIntent = new Intent("com.example.Chasse.NEW_MESSAGE");
                broadcastIntent.putExtra("message", messageContent);
                sendBroadcast(broadcastIntent);
            }
        });
    }

    public void sendMessageToSocket(String messageContent) {
        socket.emit("send message", messageContent);
        Message message = new Message(messageContent, false);
        addMessageInBackground(message);
        Intent broadcastIntent = new Intent("com.example.Chasse.NEW_MESSAGE");
        sendBroadcast(broadcastIntent);
    }

    private void clearMessagesInBackground() {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dbMessages.messageDao().clearMessages();
            }
        });
    }

    private void addMessageInBackground(Message message) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                dbMessages.messageDao().addMessage(message);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.off("receive message");
        if (executorService != null) {
            executorService.shutdown();
        }
        Intent intent = new Intent("com.example.Chasse.SERVICE_STATUS");
        intent.putExtra("isServiceRunning", false);
        sendBroadcast(intent);
    }

    public class LocalBinder extends Binder {
        public ChatService getService() {
            return ChatService.this;
        }
    }
}