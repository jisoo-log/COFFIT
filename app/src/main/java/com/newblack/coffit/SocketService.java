package com.newblack.coffit;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class SocketService extends Service {
    int status;
    final int DISCONNECTED = 0;
    final int CONNECTED = 1;


    public SocketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("TAG", "socket service created");
        this.status = CONNECTED;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.status = DISCONNECTED;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }
}
