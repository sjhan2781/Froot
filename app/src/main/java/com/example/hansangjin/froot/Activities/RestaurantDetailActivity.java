package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hansangjin.froot.Adapter.FoodRecyclerViewAdapter;
import com.example.hansangjin.froot.Data.Food;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;
import java.util.Collections;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private TextView textView_food_name, textView_food_price;
    private RatingBar ratingBar;

    private RecyclerView recyclerView_food;
    private FoodRecyclerViewAdapter foodRecyclerViewAdapter;
    private ArrayList<Food> foodList;

    private ParcelableRestaurant restaurant;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        init();

    }

    private void init(){
        createObjects();
        setUpData();
        setUpToolbar();
        setUpUI();
        setUpListener();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_not_move, R.anim.activity_right_out);
    }

    private void createObjects(){
        foodList = new ArrayList<>();

        recyclerView_food = findViewById(R.id.recyclerView_food);
    }

    private void setUpToolbar(){

    }

    private void setUpData(){
        foodList.add(new Food("a", 4000, true));
        foodList.add(new Food("b", 5000, false));
        foodList.add(new Food("c", 6000, false));
        foodList.add(new Food("d", 7000, false));
        foodList.add(new Food("e", 8000, true));

        Collections.sort(foodList);
    }

    private void setUpUI(){

        recyclerView_food.setAdapter(new FoodRecyclerViewAdapter(this, foodList));

        recyclerView_food.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
//                outRect.bottom = 40;
            }
        });


    }

    private void setUpListener(){


    }

    private void receiveIntent(){

    }

    public void setSelectedFood(Food food){

    }




}
