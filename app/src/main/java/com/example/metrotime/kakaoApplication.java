package com.example.metrotime;

import android.app.Application;
import android.util.Log;
import android.view.View;
import static com.google.firebase.messaging.Constants.TAG;

import com.kakao.sdk.auth.model.OAuthToken;
import com.kakao.sdk.common.KakaoSdk;
import com.kakao.sdk.user.UserApi;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class kakaoApplication extends Application {
    private static final String TAG = "kakaoApplication";
    @Override
    public void onCreate() {
        super.onCreate();
        // Kakao SDK 초기화
        KakaoSdk.init(this, "2f009bfb238417bd5ac1b4553b242395");
    }
}