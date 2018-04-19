package com.example.hansangjin.froot.CustomView;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hansangjin.froot.Activities.RestaurantListActivity;
import com.example.hansangjin.froot.Adapter.RestaurantListRecycleViewAdapter;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurantType;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;


public class RecyclerViewFragment extends Fragment {
    ArrayList<ParcelableRestaurant> restaurants;
    ArrayList<ParcelableRestaurantType> restaurantTypes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycler_view, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView_restaurant);

        restaurants = getArguments().getParcelableArrayList("restaurants");
        restaurantTypes = getArguments().getParcelableArrayList("restaurantTypes");

        if (!restaurants.isEmpty()){
            view.setBackground(null);
        }

       recyclerView.setAdapter(new RestaurantListRecycleViewAdapter((RestaurantListActivity) getActivity(), restaurants, restaurantTypes));

        return view;
    }
}
