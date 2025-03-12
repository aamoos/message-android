package com.example.message;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCM 메시지에서 title과 body 가져오기
        String title = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getTitle() : "No Title";
        String body = remoteMessage.getNotification() != null ? remoteMessage.getNotification().getBody() : "No Body";

        // 메시지가 포함된 알림을 보냄
        sendNotification(title, body);
    }

    private void sendNotification(String title, String body) {
        // Toast로 title과 body 출력
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                // Toast 메시지를 화면에 표시
                Toast.makeText(MyFirebaseMessagingService.this, "Title: " + title + "\nBody: " + body, Toast.LENGTH_SHORT).show();
            }
        });

        // Intent to launch MainActivity when the notification is clicked
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // PendingIntent 플래그를 API 31 이상에 맞게 설정
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE
        );

        String channelId = "My_channel_id";
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // NotificationCompat.Builder 사용하여 알림을 구성
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(android.R.drawable.ic_notification_overlay) // 사용자 정의 아이콘 사용
                        .setContentTitle(title)  // FCM에서 받은 title 사용
                        .setContentText(body)    // FCM에서 받은 body 사용
                        .setAutoCancel(true)     // 클릭 시 자동으로 알림이 제거됨
                        .setSound(defaultSoundUri) // 기본 알림 소리
                        .setContentIntent(pendingIntent);  // 알림 클릭 시 실행될 인텐트

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Android Oreo 이상에서는 알림 채널이 필요합니다.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    "Channel human readable title",  // 채널 이름
                    NotificationManager.IMPORTANCE_DEFAULT);  // 알림 중요도
            notificationManager.createNotificationChannel(channel);
        }

        // 알림을 화면에 표시
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}