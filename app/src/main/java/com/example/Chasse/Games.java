package com.example.Chasse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.Chasse.Model.Game;
import com.example.Chasse.Model.SocketManager;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public abstract class Games extends AppCompatActivity {

    protected Socket socket;
    protected Game game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        game = Game.getInstance();

        socket = SocketManager.getInstance().getSocket();

        socket.on("user disconnected", new Emitter.Listener() {
            @Override
            public void call(Object... objects) {
                runOnUiThread(() -> {
                    alertUserDisconnected();
                });
            }
        });
    }

    private void alertUserDisconnected() {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off();
    }


}
