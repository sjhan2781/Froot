package com.example.hansangjin.froot.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class RestaurantRecycleViewAdapter extends RecyclerView.Adapter<RestaurantRecycleViewAdapter.MyViewHolder>{
    ArrayList<Restaurant> restaurantList;

    public RestaurantRecycleViewAdapter(ArrayList<Restaurant> restaurantList) {
        this.restaurantList = restaurantList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_restaurant, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Toast.makeText(, "ClickListener", Toast.LENGTH_LONG).show();
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
//                Toast.makeText(RestaurantListActivity.this, "LongClickListener", Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_restaurant_name;
        private ImageView image_rastaurant;


        public MyViewHolder(View parent) {
            super(parent);

            textView_restaurant_name = parent.findViewById(R.id.restaurant_name);
        }
    }
}
