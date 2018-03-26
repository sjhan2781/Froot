package com.example.hansangjin.froot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.CustomView.GradientTextView;
import com.example.hansangjin.froot.R;
import com.nex3z.flowlayout.FlowLayout;

import java.util.ArrayList;

public class RestaurantInfoActivity extends AppCompatActivity {
    RecyclerView recyclerView_restaurant_image;
    TextView textView_restaurant_name, textView_rastaurant_location;
    RatingBar ratingBar_restaurant;
    FlowLayout flowLayout;

    ArrayList<String> restaurant_features;
//    ArrayList<>

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_info);

        init();
    }

    private void init(){
        creatObjects();
        setUpToolbar();
        setUpData();
        setUpUI();
        setUpListener();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_not_move, R.anim.activity_right_out);
    }

    private void creatObjects() {
        recyclerView_restaurant_image = findViewById(R.id.recyclerView_restaurant);
        textView_restaurant_name = findViewById(R.id.textView_restaurant_name);
        textView_rastaurant_location = findViewById(R.id.textView_restaurant_location);
        ratingBar_restaurant = findViewById(R.id.ratingBar_restaurant);
        flowLayout = findViewById(R.id.flowLayout);

        restaurant_features = new ArrayList<>();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        ImageView toolbar_start_image = findViewById(R.id.toolbar_button_left);
        GradientTextView textView_title = findViewById(R.id.toolbar_textView_title);
        ImageView toolbar_end_image = findViewById(R.id.toolbar_button_right);

        setSupportActionBar(toolbar);
//        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
        textView_title.setText("맞춤 식당");
//        toolbar_end_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
 }

    private void setUpData() {
        restaurant_features.add("할랄인증");
    }

    private void setUpUI() {

        for(int i = 0; i < restaurant_features.size(); i++){
            Button button = new Button(getApplicationContext());
            button.setText(restaurant_features.get(i));
            flowLayout.addView(button);
        }

    }

    private void setUpListener() {

    }

}
