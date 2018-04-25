package com.example.hansangjin.froot;

import android.app.Activity;
import android.view.View;
import android.widget.Toast;

/**
 * Created by sjhan on 2018-03-13.
 */

public class BackPressCloseHandler {
    private long backKeyPressedTime = 0;

    private Activity activity;

    private Toast toast;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {

            activity.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        View toastView = View.inflate(activity, R.layout.toast_back_pressed, null);

        Toast toast = Toast.makeText(activity,"",Toast.LENGTH_SHORT);

        toast.setView(toastView);

        toast.show();
    }

    public void setToast(Toast toast){
        this.toast = toast;
    }

}