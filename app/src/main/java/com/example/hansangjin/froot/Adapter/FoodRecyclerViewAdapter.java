package com.example.hansangjin.froot.Adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
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
        final int curPos = position;

        holder.textView_food_name.setText(foods.get(position).getName());
        holder.textView_food_price.setText(String.valueOf(foods.get(position).getPrice()) + "원");
//        holder.imageView_food.setImageBitmap();

        if (foods.get(position).isPossible()) {
            holder.imageView_food_enable.setVisibility(View.VISIBLE);
        }else{
            holder.imageView_food_enable.setVisibility(View.INVISIBLE);

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
}