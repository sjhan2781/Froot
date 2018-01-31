package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.hansangjin.froot.PropertyManager;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;

import java.util.ArrayList;
import java.util.List;

public class KakaoSignupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        List<String> propertyKeys = new ArrayList<String>();
//        propertyKeys.add("account_email");
        propertyKeys.add("kaccount_email");
        propertyKeys.add("nickname");
        propertyKeys.add("profile_image");
        propertyKeys.add("thumbnail_image");
        propertyKeys.add("birthday");

//        propertyKeys.add("age");

        requestMe(propertyKeys);
//        requestMe();
    }

    protected void requestMe(){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("SignUp State", errorResult.toString());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                showSignUp();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("User Profile", userProfile.toString());
                String birthday = userProfile.getProperties().get("story_birthday");
                Log.d("User Profile", birthday);

                PropertyManager.getInstance(getApplicationContext()).setKakao_id(userProfile.getId());
                redirectMainActivity();
            }
        });

    }

    protected void requestMe(List<String> propertyKeys){
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Log.d("SignUp State", errorResult.toString());
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                redirectLoginActivity();
            }

            @Override
            public void onNotSignedUp() {
                showSignUp();
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.d("User Profile", userProfile.toString());

                Log.d("kakao", userProfile.toString());

//                Context context = Session.getCurrentSession().getContext();
//                String appKey = Session.getCurrentSession().getAppKey();
//                String redirectUri = Session.getCurrentSession().getRedirectUri();
//                String authCode = Session.getCurrentSession().auth;
//                String refreshToken = Session.getCurrentSession().getRefreshToken();
//                String approvalType = Session.getCurrentSession().getAuthTypes().;
//                AuthApi.requestAccessTokenInfo().get




                Log.d("User Profile", userProfile.getId() + "");

                PropertyManager.getInstance(getApplicationContext()).setKakao_id(userProfile.getId());


                redirectMainActivity();
            }
        }, propertyKeys, false);

    }

    private void addToFirebase(){

        String kakaoAccessToken = Session.getCurrentSession().getAccessToken();




    }



    protected void redirectLoginActivity(){
        final Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    protected void redirectMainActivity(){
        startActivity(new Intent(this, CategoryActivity.class));
        finish();
    }

    protected void showSignUp(){
        redirectLoginActivity();
    }
}
