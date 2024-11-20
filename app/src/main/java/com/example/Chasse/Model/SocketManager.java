package com.example.Chasse.Model;

import android.app.Application;
import io.socket.client.IO;
import io.socket.client.Socket;

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
            socket = IO.socket("http://10.0.2.2:55557");
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

    public Socket getSocket() {
        return socket;
    }
}
