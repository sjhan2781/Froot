package com.example.hansangjin.froot.Listener;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;

/**
 * Created by hansangjin on 2018. 2. 6..
 */

public class RestaurantRecyclerViewFlingListener extends RecyclerView.OnFlingListener {
    RecyclerView recyclerView;
    RestaurantMapActivity activity;

    public RestaurantRecyclerViewFlingListener(RestaurantMapActivity activity, RecyclerView recyclerView) {
        super();
        this.activity = activity;
        this.recyclerView = recyclerView;
    }


    @Override
    public boolean onFling(int velocityX, int velocityY) {
        Log.d("fling", velocityX + "");
        int position = 0;

        if (velocityX > 0){
            position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastVisibleItemPosition();
        }
        else {
            position = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();
        }

        recyclerView.smoothScrollToPosition(position);
        activity.animateCamera(position);
        Log.d("position", position + "");

        return true;
    }

}
