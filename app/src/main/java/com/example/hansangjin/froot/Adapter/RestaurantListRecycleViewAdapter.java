package com.example.hansangjin.froot.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Activities.RestaurantDetailActivity;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurantType;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;


public class RestaurantListRecycleViewAdapter extends RecyclerView.Adapter<RestaurantListRecycleViewAdapter.MyViewHolder> {
    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<ParcelableRestaurantType> restaurantTypeList;
    private Activity activity;

    public RestaurantListRecycleViewAdapter(Activity activity, ArrayList<ParcelableRestaurant> restaurantList, ArrayList<ParcelableRestaurantType> restaurantTypeList) {
        this.restaurantList = restaurantList;
        this.restaurantTypeList = restaurantTypeList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_restaurant_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        int halal_mark_resource_id = 0;
        int image_width = holder.image_rastaurant.getLayoutParams().width;
        int image_height = holder.image_rastaurant.getLayoutParams().height;

        holder.textView_restaurant_name.setText(restaurantList.get(position).getName());
        holder.textView_restaurant_category.setText(restaurantTypeList.get(restaurantList.get(position).getCategory()).getType());
        holder.textView_recommended_count.setText(String.format("%s개", restaurantList.get(position).getFoods().split(",").length));


        if(!restaurantList.get(position).getImage_base64().isEmpty()) {
            holder.image_rastaurant.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(restaurantList.get(position).getImage_base64()), image_width, image_height, true));
        }

        switch (restaurantList.get(position).getHalal()) {
            case 1:
                halal_mark_resource_id = R.drawable.image_halal_certified;
                break;
            case 2:
                halal_mark_resource_id = R.drawable.image_self_certified;
                break;
            case 3:
                halal_mark_resource_id = R.drawable.image_muslim_friendly;
                break;
            case 4:
                halal_mark_resource_id = R.drawable.image_pork_free;
                break;
            default:

                break;
        }

        holder.imageView_halal_mark.setImageResource(halal_mark_resource_id);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RestaurantDetailActivity.class);
                intent.putExtra("restaurant", (Parcelable) restaurantList.get(position));

//                Log.d("fooood", restaurantList.get(position).getFoods());
//                intent.putExtra("foods", restaurantList.get(position).getFoods());
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
        private ImageView image_rastaurant, imageView_halal_mark;


        public MyViewHolder(View parent) {
            super(parent);

            this.image_rastaurant = parent.findViewById(R.id.imageView_restaurant);
            this.textView_restaurant_name = parent.findViewById(R.id.textView_restaurant_name);
            this.textView_restaurant_category = parent.findViewById(R.id.textView_restaurant_category);
            this.textView_recommended_count = parent.findViewById(R.id.textView_recommended_count);
            this.imageView_halal_mark = parent.findViewById(R.id.imageView_halal_mark);
        }
    }

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }


}
