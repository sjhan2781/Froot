package com.example.hansangjin.froot;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hansangjin on 2018. 1. 16..
 */

public class PropertyManager {
    //일반 사용자 프로필 정보//
    private static final String KEY_USERNAME = "user_name";
    private static final String KEY_PROFILEIMAGEURL = "user_profileimageurl";
    private static final String KEY_USEREMAIL = "user_email";
    private static final String KEY_USERID = "user_id";
    private static final String KEY_USERGENDER = "user_gender";

    //SNS, FCM등의 토큰//
    private static final String KEY_FACEBOOK_ID = "facebookid";
    private static final String KEY_KAKAO_ID = "kakao_id";
    private static final String KEY_FACEBOOK_TOKEN = "facebook_token";
    private static final String KEY_FCM_REG_ID = "fcmtoken";
    private static final String ALARM_BADGE_NUMBER = "alarm_badge_number"; //배지//

    //FCM알람관련 뱃지카운터.//
    private static int badge_number = 0;

    private static PropertyManager instance;

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEdittor;

    public PropertyManager(Context context) {

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEdittor = mPrefs.edit();
    }

    public static PropertyManager getInstance(Context context) {
        if (instance == null) {
            instance = new PropertyManager(context);
        }

        return instance;
    }

    //프래퍼런스에 저장된 값들을 불러온다.//
    public String get_user_name() {
        return mPrefs.getString(KEY_USERNAME, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_name(String user_name) {
        mEdittor.putString(KEY_USERNAME, user_name);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_id() {
        return mPrefs.getString(KEY_USERID, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_id(String user_id) {
        mEdittor.putString(KEY_USERID, user_id);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_email() {
        return mPrefs.getString(KEY_USEREMAIL, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_email(String user_email) {
        mEdittor.putString(KEY_USEREMAIL, user_email);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_profileimageurl() {
        return mPrefs.getString(KEY_PROFILEIMAGEURL, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_profileimageurl(String user_profileimageurl) {
        mEdittor.putString(KEY_PROFILEIMAGEURL, user_profileimageurl);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_gender() {
        return mPrefs.getString(KEY_USERGENDER, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_gender(String user_gender) {
        mEdittor.putString(KEY_USERGENDER, user_gender);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_facebookid() {
        return mPrefs.getString(KEY_FACEBOOK_ID, null); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_facebookid(String user_facebookid) {
        mEdittor.putString(KEY_FACEBOOK_ID, user_facebookid);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public String get_user_fcmtoken() {
        return mPrefs.getString(KEY_FCM_REG_ID, ""); //만약에 프래퍼런스가 없을 경우 ""로 나온다.//
    }

    public void set_user_fcmtoken(String user_fcmtoken) {
        mEdittor.putString(KEY_FCM_REG_ID, user_fcmtoken);
        mEdittor.commit(); //저장 후 완료한다.//
    }

    public int get_badge_number() {
        return mPrefs.getInt(ALARM_BADGE_NUMBER, badge_number);
    }

    public void setBadge_number(int badge_number) {
        mEdittor.putInt(ALARM_BADGE_NUMBER, badge_number);
        mEdittor.commit();
    }

    public Long getKakao_id() {
        return mPrefs.getLong(KEY_KAKAO_ID, 0);
    }

    public void setKakao_id(long kakao_id) {
        mEdittor.putLong(KEY_KAKAO_ID, kakao_id);
        mEdittor.commit();
    }

    public String getFacebookToken() {
        return mPrefs.getString(KEY_FACEBOOK_TOKEN, null);
    }

    public void setFacebookToken(String facebook_token) {
        mEdittor.putString(KEY_FACEBOOK_TOKEN, facebook_token);
        mEdittor.commit();
    }

}

