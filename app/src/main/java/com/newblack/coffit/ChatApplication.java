package com.newblack.coffit;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.github.nkzawa.engineio.client.transports.WebSocket;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Manager;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;
import java.util.logging.Level;

public class ChatApplication extends Application {
    private static ChatApplication INSTANCE;
    private Socket mSocket;
    private static final String URL = "https://api.coffitnow.com/chattings";

    @Override
    public void onCreate() {
        super.onCreate();
        AndroidLoggingHandler.reset(new AndroidLoggingHandler());
        java.util.logging.Logger.getLogger(Socket.class.getName()).setLevel(Level.FINEST);
//        java.util.logging.Logger.getLogger(io.socket.engineio.client.Socket.class.getName()).setLevel(Level.FINEST);
        java.util.logging.Logger.getLogger(Manager.class.getName()).setLevel(Level.FINEST);

//        Intent background = new Intent(this, SocketService.class);
//        startService(background);

        try{
            IO.Options opts = new IO.Options();
            opts.transports = new String[] {"websocket"};
            Log.d("TAG", WebSocket.NAME);
            mSocket = IO.socket(URL, opts);
            Log.d("TAG", "ChatApplication : socket connection tried");
        } catch (URISyntaxException e){
            Log.d("TAG", "ChatApplication : has problem with socket connection");
            throw new RuntimeException(e);
        }
    }

    public Socket getmSocket() {
        if(mSocket==null){
            Log.d("TAG","socket is null");
        }
        Log.d("TAG", "ChatApplication : get socket");
        return mSocket;
    }
}
