package com.example.hansangjin.froot.Listener;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;
import com.example.hansangjin.froot.Data.Restaurant;
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
        activity.setSelectedChild(((Restaurant) marker.getTag()).getID());

        return true;
    }
}
