package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.R;
import com.google.firebase.auth.FirebaseAuth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity implements View.OnClickListener {
    Handler handler = new Handler();

    Class nextActivity;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ApplicationController.metrics = metrics;

        Log.d("metrics", ApplicationController.metrics.toString());

        getHashKey();

        findViewById(R.id.button_start).setOnClickListener(this);

//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                ApplicationController.facebookAccessToken = AccessToken.getCurrentAccessToken();
//
//                PropertyManager instance = PropertyManager.getInstance(getApplicationContext());
//
//                String facebookToken = instance.getFacebookToken();
//                long kakaoID = instance.getKakao_id();
//
//                Log.d("facebook", "로그인 되어있음 " + facebookToken + "aa");
//                Log.d("kakao", "로그인 되어있음 " + kakaoID + "aa");
//
//                if((facebookToken != null) | (kakaoID != 0)){
//                    Log.d("facebook", "로그인 되어있음 " + facebookToken + "aa");
//                    Log.d("kakao", "로그인 되어있음 " + kakaoID + "aa");
//
//                    nextActivity = CategoryActivity.class;
//
//                    GraphRequest request = GraphRequest.newMeRequest(ApplicationController.facebookAccessToken,
//                            new GraphRequest.GraphJSONObjectCallback() {
//                                @Override
//                                public void onCompleted(JSONObject object, GraphResponse response) {
//                                    try {
//                                        Log.d("user profile", object.toString());
//                                        Log.d("user profile", object.getString("name"));
//
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                    request.executeAsync();
//                }
//                else{
//                    nextActivity = LoginActivity.class;
//                }
//                nextActivity = RestaurantMapActivity.class;
//
//                Intent intent = new Intent(SplashActivity.this, nextActivity);
//                startActivity(intent);          //다음 액티비티로 전환
//                finish();   //현재 액티비티를 종료
//            }
//        }, 3000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_start:
                nextActivity = CategoryActivity.class;

                Intent intent = new Intent(SplashActivity.this, nextActivity);
                startActivity(intent);          //다음 액티비티로 전환
                finish();   //현재 액티비티를 종료
                break;
        }
    }

    private void getHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));

            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
