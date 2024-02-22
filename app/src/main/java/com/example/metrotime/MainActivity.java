package com.example.metrotime;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        askNotificationPermission();
        logRegToken();
        runtimeEnableAutoInit();

        /** 카카오 로그인 버튼 **/
        ImageView moveButton = findViewById(R.id.btn_kakaoLogin);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 카카오톡이 핸드폰에 설치되어 있는지 확인
                if (UserApiClient.getInstance().isKakaoTalkLoginAvailable(MainActivity.this)) {
                    UserApiClient.getInstance().loginWithKakaoTalk(MainActivity.this, callback);
                } else { // 카카오톡 설치 X :
                    UserApiClient.getInstance().loginWithKakaoAccount(MainActivity.this, callback);
                }
                // Intent intent = new Intent(getApplicationContext(), MainActivity2.class); // 페이지 이동 코드..되려나
                // startActivity(intent);
            }
        });
    }
    Function2<OAuthToken, Throwable, Unit> callback = new Function2<OAuthToken, Throwable, Unit>() {
        @Override
        public Unit invoke(OAuthToken oAuthToken, Throwable throwable) {
            // 이때 토큰 전달이 되면 로그인 성공 , 토큰 전달 안 되면 로그인 실패
            updateKakaoLoginUI();
            return null;
        }
    };
    private void updateKakaoLoginUI() {
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {
                // 로그인 되어있으면
                if (user != null) {
                    // 유저 아이디
                    Log.d(TAG, "invoke: id" + user.getId());
                    // 유저 이메일
                    Log.d(TAG, "invoke: email" + user.getKakaoAccount().getEmail());
                    // 유저 닉네임
                    Log.d(TAG, "invoke: nickname" + user.getKakaoAccount().getProfile().getNickname());
                } else {
                    // 로그인 안 되어있을 때
                    Log.d(TAG, "로그인되지 않은 유저입니다");
                }
                return Unit.INSTANCE;
            }
        });
    }
    // Android 13 이상에서 런타임 알림 권한 요청
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // FCM SDK (and your app) can post notifications.
                } else {
                    // TODO: Inform user that that your app will not show notifications.
                }
            });
    // Android 앱에서 자동 초기화 활성
    public void runtimeEnableAutoInit() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
    }
    //알림 권한 화면 표시
    private void askNotificationPermission() {
        // API 33 이상에서 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // FCM SDK와 앱에서 알림 표시 가능해짐
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
            } else {
                // 권한 직접 요청
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        }
    }
    private void logRegToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "FCM 토큰 가져오기 실패", task.getException());
                            return;
                        }

                        // 새로 토큰 가져오기
                        String token = task.getResult();

                        // Log and toast
                        String msg = "FCM token : " + token;
                        Log.d(TAG, msg);
                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void updateUI(@Nullable FirebaseUser user) {
        // No-op
    }
}