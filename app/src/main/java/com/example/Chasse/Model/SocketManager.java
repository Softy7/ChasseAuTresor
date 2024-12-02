package com.example.Chasse.Model;

import android.app.Application;
import android.util.Log;
import com.example.Chasse.Model.System.MainSystem;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import java.net.URISyntaxException;

public class SocketManager{
    private static SocketManager instance;
    private Socket socket;


    public SocketManager(){
        instance = this;
        initializeSocket();
    }

    private void initializeSocket() {
        try {
            IO.Options options = new IO.Options();
            options.reconnection = true;
            options.reconnectionAttempts = 100000;
            options.reconnectionDelay = 1000;
            options.reconnectionDelayMax = 5000;
            options.timeout = 20000;
            String addressNodejsServor = new MainSystem().getAddressNodejsServor();
            if (addressNodejsServor.endsWith("/")) {
                addressNodejsServor = addressNodejsServor.substring(0, addressNodejsServor.length() - 1);
            }
            socket = IO.socket(addressNodejsServor);
            socket.connect();


            socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... objects) {
                    Log.d("socket", "Connecté au serveur!");
                }
            }).on("reconnect", new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    Log.d("socket", "Reconnexion du serveur");
                    Game game = Game.getInstance();
                    if (game.getUserId() != -1){
                        socket.emit("reconnect", game.getUserId(), game.getCode());
                    }
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

                @Override
                public void call(Object... objects) {
                    Log.d("socket", "Déconnexion du serveur");
                }
            });

        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    public static SocketManager newInstance() {
        instance = new SocketManager();
        return instance;
    }

    public static void destroyInstance() {
        instance = null;
    }

    public Socket getSocket() {
        return socket;
    }
}
