package com.example.message;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // 권한 요청 콜백 정의 (알림 권한 요청)
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
                @Override
                public void onActivityResult(Boolean isGranted) {
                    if (isGranted) {
                        // 권한이 승인되었을 때
                        Toast.makeText(MainActivity.this, "알림 권한이 승인되었습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        // 권한이 거부되었을 때
                        Toast.makeText(MainActivity.this, "알림 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

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
                        // 토큰을 서버에 전송합니다.
                        sendTokenToServer(token);
                    } else {
                        Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                    }
                });

        // 안드로이드 13(API 33) 이상에서는 알림 권한 요청이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // 알림 권한이 승인되지 않은 경우 권한 요청
            if (checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }

    // 서버에 토큰을 전송하는 메소드
    private void sendTokenToServer(String token) {
        // 서버에 토큰을 전송하는 코드 작성
        // 예: HTTP 요청을 보내서 Spring 서버에 토큰을 전달합니다.
    }
}
