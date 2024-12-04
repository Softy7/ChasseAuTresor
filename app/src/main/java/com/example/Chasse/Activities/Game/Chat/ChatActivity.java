package com.example.Chasse.Activities.Game.Chat;


import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.Model.Message;
import com.example.Chasse.Model.SQLite.MessageDatabase;
import com.example.Chasse.R;

import java.util.ArrayList;
import java.util.List;



public class ChatActivity extends GlobalTresorActivity {

    private RecyclerView chatRecyclerView;
    private EditText messageEditText;
    private Button sendButton;
    private MessageAdapter messageAdapter;
    private ChatService chatService;
    private boolean isBound = false;
    private ChatViewModel chatViewModel;
    private List<Message> messageList = new ArrayList<>();

    private final ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            ChatService.LocalBinder binder = (ChatService.LocalBinder) service;
            chatService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }
    };
    private final BroadcastReceiver closeReceiver = new CloseReceiver();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageEditText = findViewById(R.id.message);
        sendButton = findViewById(R.id.sendButton);

        chatViewModel = new ViewModelProvider(this).get(ChatViewModel.class);
        messageAdapter = new MessageAdapter(this, new ArrayList<>());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(messageAdapter);

        chatViewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
                chatRecyclerView.scrollToPosition(messages.size() - 1);
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageContent = messageEditText.getText().toString();
                if (!messageContent.isEmpty()) {
                    sendMessageToSocket(messageContent);
                    messageEditText.setText("");
                }
            }
        });

        Intent intent = new Intent(this, ChatService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter closeFilter = new IntentFilter("com.example.Chasse.CLOSE_CHAT");
        registerReceiver(closeReceiver, closeFilter, Context.RECEIVER_EXPORTED);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(closeReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private void sendMessageToSocket(String messageContent) {
        if (chatService != null) {
            chatService.sendMessageToSocket(messageContent);
        }
    }




}
