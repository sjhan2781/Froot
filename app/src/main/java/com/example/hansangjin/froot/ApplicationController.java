package com.example.hansangjin.froot;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import com.example.hansangjin.froot.Kakao.KakaoSDKAdapter;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.kakao.auth.KakaoSDK;
import com.kakao.auth.Session;

import java.util.Locale;

/**
 * Created by hansangjin on 2018. 1. 15..
 */

public class ApplicationController extends Application {
    private static ApplicationController instance = null;
    private static volatile Activity currentActivity = null;

    public static AccessToken facebookAccessToken = null;
    public static String kakaoAccessToken = null;

    public static ApplicationController getInstance() {
        return instance;
    }

    public static DisplayMetrics metrics;

    /**
     * 이미지 로더, 이미지 캐시, 요청 큐를 초기화한다.
     */
    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationController.instance = this;

        metrics = getApplicationContext().getResources().getDisplayMetrics();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        KakaoSDK.init(new KakaoSDKAdapter());

        facebookAccessToken = AccessToken.getCurrentAccessToken();
        kakaoAccessToken = Session.getCurrentSession().getAccessToken();
    }

    public static Activity getCurrentActivity() {
        return currentActivity;
    }

    public static void setCurrentActivity(Activity activity) {
        ApplicationController.currentActivity = activity;
    }

    public static ApplicationController getApplicationController() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    /**
     * 애플리케이션 종료시 singleton 어플리케이션 객체 초기화한다.
     */

    public static void setLocale(String charset, String country) {
        Locale locale = new Locale(charset, country);
        Configuration config = new Configuration();
        config.locale = locale;
        getInstance().getResources().updateConfiguration(config, null);
    }

    //이미지 크기 변환
    public static Bitmap setUpImage(int resID) {
        double ratio = (double) (metrics.densityDpi / 160) / 4;
        Bitmap bitmap = BitmapFactory.decodeResource(instance.getResources(), resID);
        int width = (int) (bitmap.getWidth() * ratio);

        int height = (int) (bitmap.getHeight() * ratio);

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    //drawable 크기 변환
    public static Drawable setUpDrawable(int resID) {
        Bitmap bitmap = setUpImage(resID);

        return new BitmapDrawable(instance.getResources(), bitmap);
    }

    public static void startActivity(Activity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.activity_right_in, R.anim.activity_not_move);
    }

    public static void finish(Activity activity) {
        activity.finish();
        activity.overridePendingTransition(R.anim.activity_not_move, R.anim.activity_right_out);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}
