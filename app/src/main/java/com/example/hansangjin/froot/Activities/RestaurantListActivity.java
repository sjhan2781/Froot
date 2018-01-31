package com.example.hansangjin.froot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.hansangjin.froot.Adapter.RestaurantRecycleViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class RestaurantListActivity extends AppCompatActivity {
    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private RecyclerView restaurant_recyclerView;
    private Spinner spinner_kind, spinner_location;


    private ArrayList<Restaurant> restaurantList;
    private ArrayList<String> kindList;
    private ArrayList<String> locationList;

    private RestaurantRecycleViewAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        init();
    }

    private void init(){
        creatObjects();
        setUpToolbar();
        setUpData();
        setUpUI();
    }

    private void creatObjects(){
        toolbar_start_image = findViewById(R.id.toolbar_button_first);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_second);

        restaurant_recyclerView = findViewById(R.id.recyclerView_restaurant);
        spinner_kind = findViewById(R.id.spinner_kind);
        spinner_location = findViewById(R.id.spinner_location);

        restaurantList = new ArrayList<>();
        kindList = new ArrayList<>();
        locationList = new ArrayList<>();
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
        textView_title.setText("맞춤 식당");
        toolbar_end_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
    }

    private void setUpData(){
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());
        restaurantList.add(new Restaurant());

        kindList.add("한식");
        kindList.add("중식");
        kindList.add("일식");

        locationList.add("인사동");
        locationList.add("명동");

    }

    private void setUpUI(){
        restaurant_recyclerView.setAdapter(new RestaurantRecycleViewAdapter(restaurantList));
        ArrayAdapter spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, locationList);
        spinner_location.setAdapter(spinnerAdapter);
        spinnerAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, kindList);
        spinner_kind.setAdapter(spinnerAdapter);
    }


}
