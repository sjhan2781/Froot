package com.example.hansangjin.froot.Listener;

import android.util.Log;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/**
 * Created by hansangjin on 2018. 2. 13..
 */

public class MapMarkerClickListener implements GoogleMap.OnMarkerClickListener {
    RestaurantMapActivity activity;

    public MapMarkerClickListener(RestaurantMapActivity activity) {
        this.activity = activity;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        activity.setSelectedChild((Integer.parseInt(marker.getTitle())));
        Log.d("TTAAAGGG", String.valueOf(Integer.parseInt(marker.getTitle())));
        return true;
    }
}
