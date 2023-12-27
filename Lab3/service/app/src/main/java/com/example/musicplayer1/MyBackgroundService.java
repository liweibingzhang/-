package com.example.musicplayer1;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyBackgroundService extends Service {

    private static final String TAG = "MyBackgroundService";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Background Service Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Background Service Started");

        // 在这里执行后台任务
        performBackgroundTask();

        // 如果服务被杀死，不要重启服务
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // 如果服务不支持绑定，返回 null
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Background Service Destroyed");
    }

    private void performBackgroundTask() {
        // 在这里执行你的后台任务
        Log.d(TAG, "Performing background task...");
    }
}

