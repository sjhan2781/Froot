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
//            toast.cancel();
//            Intent t = new Intent(activity, MainActivity.class);
//            activity.startActivity(t);

//            activity.moveTaskToBack(true);
//            activity.finish();
            activity.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
        }
    }

    public void showGuide() {
        Toast ToastMessage = Toast.makeText(activity,"Change Toast Background color",Toast.LENGTH_SHORT);
        View toastView = ToastMessage.getView();
        toastView.setBackgroundResource(R.color.toast_background_color);
        ToastMessage.show();

//        toast.show();
    }

    public void setToast(Toast toast){
        this.toast = toast;
    }

}