package com.example.message;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FCM 토큰을 가져옵니다.
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String token = task.getResult();
                        Log.d(TAG, "FCM Token: " + token);
                        System.out.println("token : " + token);
                        // 토큰을 서버에 전송합니다.
                        sendTokenToServer(token);
                    } else {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    }
                });
    }

    private void sendTokenToServer(String token) {
        // 서버에 토큰을 전송하는 코드 작성
        // 예: HTTP 요청을 보내서 Spring 서버에 토큰을 전달합니다.
    }
}
