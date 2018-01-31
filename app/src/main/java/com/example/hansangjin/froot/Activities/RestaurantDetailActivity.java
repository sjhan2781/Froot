package com.example.hansangjin.froot.Activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hansangjin.froot.Adapter.FoodRecyclerViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Food;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity {
    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private TextView textView_food_name, textView_food_price;
    private RatingBar ratingBar;

    private RecyclerView recyclerView_food;
    private FoodRecyclerViewAdapter foodRecyclerViewAdapter;
    private ArrayList<Food> foodList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        init();

    }

    private void init(){
        creatObjects();
        setUpToolbar();
        setUpData();
        setUpUI();
        setUpListener();
    }

    private void creatObjects(){
        toolbar_start_image = findViewById(R.id.toolbar_button_first);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_second);

        textView_food_name = findViewById(R.id.textView_food_name);
        textView_food_price = findViewById(R.id.textView_food_price);
        recyclerView_food = findViewById(R.id.recyclerView_food);
        ratingBar = findViewById(R.id.ratingBar);

        foodList = new ArrayList<>();
        foodRecyclerViewAdapter = new FoodRecyclerViewAdapter(this, foodList);
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
        textView_title.setText("맞춤 식당");
        toolbar_end_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
    }

    private void setUpData(){

        foodList.add(new Food("a", 1000));
        foodList.add(new Food("b", 2000));
        foodList.add(new Food("c", 3000));
        foodList.add(new Food("d", 4000));
        foodList.add(new Food("e", 5000));

        ratingBar.setRating(2.5f);

    }

    private void setUpUI(){
        recyclerView_food.setAdapter(foodRecyclerViewAdapter);
        recyclerView_food.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 20;
                outRect.bottom = 10;
            }
        });

        setSelectedFood(foodList.get(0));

    }

    private void setUpListener(){
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating(rating);
            }
        });

    }

    private void receiveIntent(){

    }

    public void setSelectedFood(Food food){
        String won = getResources().getString(R.string.Won);
        textView_food_name.setText(food.getName());
        textView_food_price.setText(food.getPrice() + won);
    }



}
