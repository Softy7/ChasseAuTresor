package com.example.Chasse.Activities.Game;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.example.Chasse.Activities.Game.Chat.ChatActivity;
import com.example.Chasse.Activities.Game.Chat.ChatService;
import com.example.Chasse.Activities.GlobalTresorActivity;
import com.example.Chasse.Model.Game;
import com.example.Chasse.Model.SocketManager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static androidx.fragment.app.FragmentManager.TAG;

public abstract class Games extends GlobalTresorActivity {

    protected Socket socket;
    protected Game game;
    protected boolean isTheGameFinished = true;
    protected static final String IS_THE_MAIN_USER = "isTheMainUser";
    protected static final int NUMBER_OF_MINI_GAMES = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        game = Game.getInstance();
        socket = SocketManager.getInstance().getSocket();


        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                disconnectSocket();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        socket.on("user disconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                runOnUiThread(() -> {
                    alertUserDisconnected();
                });
            }
        });


    }

    private void disconnectSocket() {
        if (socket != null && socket.connected()) {
            socket.disconnect();
            socket.off();
        }
        SocketManager.destroyInstance();
    }

    private void alertUserDisconnected() {
        if (!isFinishing() && !isDestroyed()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Alerte");
            builder.setMessage("L'autre joueur a malheureusement quitter le jeu, le jeu est donc fini.");
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                }
            });
            builder.setCancelable(false);
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    protected void onPreDestroy() {}

    @Override
    protected void onDestroy() {
        Intent closeChatIntent = new Intent("com.example.Chasse.CLOSE_CHAT");
        sendBroadcast(closeChatIntent);

        onPreDestroy();
        if (isTheGameFinished){
            disconnectSocket();
        }

        super.onDestroy();
    }


}
