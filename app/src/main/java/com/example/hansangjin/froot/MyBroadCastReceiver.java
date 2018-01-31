package com.example.hansangjin.froot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class MyBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        switch (action){
            case "FoodCardViewClick" :
//                intent.
                break;
        }
    }
}
