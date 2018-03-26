package com.example.hansangjin.froot.Adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Activities.RestaurantDetailActivity;
import com.example.hansangjin.froot.Activities.RestaurantListActivity;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;



public class RestaurantListRecycleViewAdapter extends RecyclerView.Adapter<RestaurantListRecycleViewAdapter.MyViewHolder>{
    private ArrayList<ParcelableRestaurant> restaurantList;
    private RestaurantListActivity activity;

    public RestaurantListRecycleViewAdapter(RestaurantListActivity activity, ArrayList<ParcelableRestaurant> restaurantList) {
        this.restaurantList = restaurantList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_restaurant_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.textView_restaurant_name.setText(restaurantList.get(position).getName());
        holder.textView_restaurant_category.setText(restaurantList.get(position).getCategory() + "");
        holder.textView_recommended_count.setText(restaurantList.get(position).getFoods().size() + "개");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestaurantDetailActivity.class);
                ApplicationController.startActivity(activity, intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView textView_restaurant_name, textView_restaurant_category, textView_recommended_count;
        private ImageView image_rastaurant;


        public MyViewHolder(View parent) {
            super(parent);

            this.image_rastaurant = parent.findViewById(R.id.imageView_restaurant);
            this.textView_restaurant_name = parent.findViewById(R.id.textView_restaurant_name);
            this.textView_restaurant_category = parent.findViewById(R.id.textView_restaurant_category);
            this.textView_recommended_count = parent.findViewById(R.id.textView_recommended_count);
        }
    }
}
