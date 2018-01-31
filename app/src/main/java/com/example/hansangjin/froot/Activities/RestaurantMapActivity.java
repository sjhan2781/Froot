package com.example.hansangjin.froot.Activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Fragment.NaverMapFragment;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class RestaurantMapActivity extends AppCompatActivity {
    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private NaverMapFragment mapFragment;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        init();
    }



    private void init(){
        createObject();
        setUpToolbar();
        setUpData();
        setUpNaverMap();
        setUpUI();
    }

    private void createObject(){
        recyclerView = findViewById(R.id.restaurant_list_view);
        toolbar_start_image = findViewById(R.id.toolbar_button_first);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_second);

        restaurantList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter();

    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
        textView_title.setText("주변 식당 찾기");
        toolbar_end_image.setVisibility(View.GONE);
//        toolbar_end_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
    }

    private void setUpData(){

    }

    private void setUpNaverMap(){
        mapFragment = new NaverMapFragment();
        mapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.naver_map_fragment, mapFragment);
        fragmentTransaction.commit();
    }

    private void setUpUI(){
        recyclerView.setAdapter(new RecyclerViewAdapter());
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_layout, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Toast.makeText(RestaurantMapActivity.this, "ClickListener", Toast.LENGTH_LONG).show();
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(RestaurantMapActivity.this, "LongClickListener", Toast.LENGTH_LONG).show();

                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return restaurantList.size();
        }

    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        private TextView store_name_textView;
        private ImageView store_image;


        public MyViewHolder(View parent) {
            super(parent);

            this.store_name_textView = parent.findViewById(R.id.store_name_textView);
        }
    }
}
