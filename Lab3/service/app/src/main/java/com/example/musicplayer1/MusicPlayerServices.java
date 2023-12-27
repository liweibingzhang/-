package com.example.musicplayer1;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.service.R;

import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerServices extends Service {

    private MediaPlayer mediaPlayer; // 多媒体对象
    private Timer timer; // 时钟对象
    // 前台服务通知渠道ID
    private static final String CHANNEL_ID = "music_channel";
    public MusicPlayerServices() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicControl(); // 绑定服务的时候，把音乐控制类实例化
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 实例化多媒体对象
        mediaPlayer = new MediaPlayer();
        createNotificationChannel();
    }

    // 构建前台服务的通知
    private Notification buildNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("音乐播放服务")
                .setContentText("巨人，启动！")
                .setSmallIcon(R.drawable.music);
        return builder.build();
    }

    // 创建通知渠道（仅在Android 8.0及以上版本需要）
    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "音乐播放通知",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            // 设置通知渠道的描述
            channel.setDescription("用于显示正在播放音乐的通知");
            notificationManager.createNotificationChannel(channel);
            startForeground(1, buildNotification());
        }
    }
    // 启动后台服务
    private void startBackgroundService() {
        Intent backgroundServiceIntent = new Intent(getApplicationContext(), MyBackgroundService.class);
        startService(backgroundServiceIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startBackgroundService();
        return super.onStartCommand(intent, flags, startId);
    }

    // 增加计时器
    public void addTimer() {
        if (timer == null) {
            timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // 获取子线程ID
                    long threadId = Thread.currentThread().getId();
                    // 在日志中输出子线程ID
                    Log.d("MusicPlayerServices", "Thread ID: " + threadId);
                    int duration = mediaPlayer.getDuration(); // 获取歌曲总时长
                    int currentPos = mediaPlayer.getCurrentPosition(); // 获取当前播放进度
                    Message msg = MainActivity.handler.obtainMessage(); // 创建消息对象
                    // 将音乐的总时长和播放进度封装至消息对象中
                    Bundle bundle = new Bundle();
                    bundle.putInt("duration", duration);
                    bundle.putInt("currentPosition", currentPos);
                    msg.setData(bundle);
                    // 将消息发送到主线程的消息队列
                    MainActivity.handler.sendMessage(msg);
                }
            };
            //开始计时任务后的5毫秒，第一次执行task任务，以后每500毫秒执行一次
            timer.schedule(task, 5, 500);
        }
    }

    public interface MusicControlCallback {
        void onMusicStarted();

        void onMusicStopped();

        void onMusicPaused();

        void onMusicResumed();
    }

    private MusicControlCallback callback;

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    class MusicControl extends Binder {
        private MusicControlCallback callback;

        public void setCallback(MusicControlCallback callback) {
            this.callback = callback;
        }
        public void setAsBackgroundService(boolean isBackground) {
            if (isBackground) {
                stopForeground(true);
            } else {
                startForeground(1, buildNotification());
            }
        }
        // 播放音乐
        public void play() {
            mediaPlayer.reset();
            mediaPlayer = MediaPlayer.create(getApplicationContext(), com.example.service.R.raw.audio);
            mediaPlayer.start();
            addTimer();
            // 通知活动音乐已经开始播放
            if (callback != null) {
                callback.onMusicStarted();
            }
        }

        // 暂停
        public void pause() {
            mediaPlayer.pause(); // 暂停音乐
            if (callback != null) {
                callback.onMusicPaused();
            }
        }

        // 继续
        public void resume() {
            mediaPlayer.start(); // 播放，不会重置
            if (callback != null) {
                callback.onMusicResumed();
            }
        }

        // 停止
        public void stop() {
            mediaPlayer.stop(); // 停止音乐
            // 通知活动音乐已经停止播放
            if (callback != null) {
                callback.onMusicStopped();
            }
            mediaPlayer.release();
            try {
                timer.cancel();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 打带
        public void seekTo(int ms) {
            mediaPlayer.seekTo(ms);
        }
    }

}
