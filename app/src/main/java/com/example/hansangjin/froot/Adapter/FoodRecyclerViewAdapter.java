package com.example.hansangjin.froot.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Activities.RestaurantDetailActivity;
import com.example.hansangjin.froot.Data.Food;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.MyViewHolder> {
    private ArrayList<Food> foods;
    private RestaurantDetailActivity activity;
    private int selectedPos = 0;

    public FoodRecyclerViewAdapter(RestaurantDetailActivity activity, ArrayList<Food> foods) {
        this.foods = foods;
        this.activity = activity;
    }

    public int getSelectedPos() {
        return selectedPos;
    }

    public void setSelectedPos(int selectedPos) {
        this.selectedPos = selectedPos;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_food, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        int image_width = holder.imageView_food.getLayoutParams().width;
        int image_height = holder.imageView_food.getLayoutParams().height;

        holder.textView_food_name.setText(foods.get(position).getName());
        holder.textView_food_price.setText(String.format("%s원", foods.get(position).getPrice()));

        if (foods.get(position).isAvailable()) {
            holder.imageView_food_enable.setVisibility(View.INVISIBLE);
        }else{
            holder.imageView_food_enable.setVisibility(View.VISIBLE);

        }

        if (!foods.get(position).getImage_base64().isEmpty()) {
            holder.imageView_food.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(foods.get(position).getImage_base64()), image_width, image_height, true));
        }

//        if (getSelectedPos() == position){
//            holder.itemView.setBackgroundColor(activity.getResources().getColor(R.color.logoColor));
//        }
//        else{
//            holder.itemView.setBackgroundColor(Color.WHITE);
//        }

//        holder.cardView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                notifyItemChanged(selectedPos);
//                selectedPos = curPos;
//                activity.setSelectedFood(foods.get(curPos));
//                notifyItemChanged(selectedPos);
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return foods.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView textView_food_name, textView_food_price;
        public ImageView imageView_food, imageView_food_enable;

        public MyViewHolder(View parent) {
            super(parent);

            cardView = parent.findViewById(R.id.cardView_food);
            textView_food_name = parent.findViewById(R.id.textView_food_name);
            textView_food_price = parent.findViewById(R.id.textView_price);
            imageView_food = parent.findViewById(R.id.imageView_food);
            imageView_food_enable = parent.findViewById(R.id.imageView_food_enable);
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