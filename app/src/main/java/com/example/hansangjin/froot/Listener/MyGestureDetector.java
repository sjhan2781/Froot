package com.example.hansangjin.froot.Listener;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * Created by hansangjin on 2018. 2. 19..
 */

public class MyGestureDetector extends GestureDetector {
    public MyGestureDetector(Context context, OnGestureListener listener) {
        super(context, listener);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        Log.d("GESTURE_MOTION", ev.toString());
        return super.onTouchEvent(ev);
    }
}
