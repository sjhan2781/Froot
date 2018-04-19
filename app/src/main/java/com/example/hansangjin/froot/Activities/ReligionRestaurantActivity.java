package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.BackPressCloseHandler;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class ReligionRestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolbar_left_image, toolbar_right_image, toolbar_image_logo;
    private RelativeLayout container_halal;

    private Intent intent;
    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<String> kindList;
    private ArrayList<String> locationList;

    private BackPressCloseHandler backPressCloseHandler;

    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list_religion);

        init();
    }

    private void init() {
        creatObjects();
        setUpData();
        setUpUI();
    }

    private void creatObjects() {
        toolbar_left_image = findViewById(R.id.toolbar_button_left);
        toolbar_right_image = findViewById(R.id.toolbar_button_right);
        toolbar_image_logo = findViewById(R.id.toolbar_image_logo);
        container_halal = findViewById(R.id.container_halal);

        restaurantList = new ArrayList<>();
        kindList = new ArrayList<>();
        locationList = new ArrayList<>();

        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void setUpData() {
        restaurantList.add(new ParcelableRestaurant(0, "게코스 테라스", 1));
        restaurantList.add(new ParcelableRestaurant(1, "구월당", 2));
        restaurantList.add(new ParcelableRestaurant(2, "adsasd", 2));
        restaurantList.add(new ParcelableRestaurant(3, "ffffff", 3));

        kindList.add("전체");
        kindList.add("한식");
        kindList.add("중식");
        kindList.add("일식");
        kindList.add("양식");
        kindList.add("세계음식");

        locationList.add("인사동");
        locationList.add("명동");
        locationList.add("홍대");
        locationList.add("이태원");
        locationList.add("강남");

    }

    private void setUpUI() {
        setUpToolbar();
        setUpToast();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_left_image.setVisibility(View.VISIBLE);
        toolbar_left_image.setImageResource(R.drawable.ic_account_circle_black_36dp);
        toolbar_left_image.setOnClickListener(this);
        toolbar_right_image.setVisibility(View.VISIBLE);
        toolbar_right_image.setImageResource(R.drawable.ic_place_black_36dp);
        toolbar_right_image.setOnClickListener(this);
        container_halal.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == toolbar_right_image) {
            intent = new Intent(this, RestaurantMapActivity.class);
            intent.putExtra("restaurants", restaurantList);
            ApplicationController.startActivity(this, intent);
        } else if (v == toolbar_left_image) {
            ApplicationController.finish(this);
        } else if (v == container_halal) {
            intent = new Intent(this, HalalExplainActivity.class);
            ApplicationController.startActivity(this, intent);
        }
    }

    @Override
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

    private void setUpToast() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.layout_toast));
        TextView textView = view.findViewById(R.id.textView_toast);
        toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.FILL_HORIZONTAL | Gravity.BOTTOM, 0, 0);
        textView.setText("한번 더 누르시면 종료됩니다");
        toast.setView(view);

        backPressCloseHandler.setToast(toast);
    }
}
