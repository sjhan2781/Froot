package com.example.hansangjin.froot.Listener;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearSmoothScroller;

/**
 * Created by hansangjin on 2018. 2. 19..
 */

public class RestaurantRecyclerViewSmoothScroller extends LinearSmoothScroller {
    public RestaurantRecyclerViewSmoothScroller(Context context) {
        super(context);
    }

    @Nullable
    @Override
    public PointF computeScrollVectorForPosition(int targetPosition) {
        return super.computeScrollVectorForPosition(targetPosition);
    }
}
