package com.example.hansangjin.froot.Listener;

import android.support.v7.widget.RecyclerView;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;


/**
 * Created by hansangjin on 2018. 2. 19..
 */

public class ScrollListener extends RecyclerView.OnScrollListener {
    int bound;
    int scrollExtent;
    int selected_position;
    RestaurantMapActivity activity;

    public ScrollListener(RestaurantMapActivity activity, int scrollExtent) {
        this.scrollExtent = scrollExtent;
        this.activity = activity;
        this.bound = scrollExtent / 2;
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

        if(recyclerView.getScrollState() == RecyclerView.SCROLL_INDICATOR_RIGHT){
            recyclerView.smoothScrollToPosition(selected_position);
        }
//        Log.d("ScrollState", newState + "");

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        bound = recyclerView.computeHorizontalScrollExtent()/2;

        selected_position = (recyclerView.computeHorizontalScrollOffset() + bound) / recyclerView.computeHorizontalScrollExtent();

//        if(recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING){
//        Log.d("adsasdadadsasad", recyclerView.getScrollState() + "");
//        }

        activity.setSelectedChild(selected_position);

    }
}
