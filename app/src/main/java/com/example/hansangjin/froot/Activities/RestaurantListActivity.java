package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;

import com.example.hansangjin.froot.Adapter.CustomPagerAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.BackPressCloseHandler;
import com.example.hansangjin.froot.CustomView.GradientTextView;
import com.example.hansangjin.froot.CustomView.MyBottomSheetDialogFragment;
import com.example.hansangjin.froot.CustomView.RecyclerViewFragment;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class RestaurantListActivity extends AppCompatActivity implements View.OnClickListener, TabHost.OnTabChangeListener {
    private ImageView toolbar_left_image, toolbar_right_image, drop_down_image;
    private GradientTextView textView_title;

    private Intent intent;
    private int type;
    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<String> kindList;
    private ArrayList<String> locationList;

    private View bottomSheet;
    private BottomSheetBehavior behavior;
    private BottomSheetDialogFragment myBottomSheet;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        type = intent.getIntExtra("category_type", -1);

        switch (type) {
            case 0:
                setContentView(R.layout.activity_restaurant_list_religion);
                break;
            case 1:
            case 2:
            default:
                setContentView(R.layout.activity_restaurant_list);
                break;
        }

        init();
    }

    private void init() {
        creatObjects();
        setUpData();
        setUpUI();
    }

    private void creatObjects() {
        restaurantList = new ArrayList<>();
        kindList = new ArrayList<>();
        locationList = new ArrayList<>();

        myBottomSheet = MyBottomSheetDialogFragment.newInstance(locationList);

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
        setUpToolbar(type);

        if (type != 0) {
            setUpTabView();
        }

    }

    private void setUpToolbar(int type) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_left_image = findViewById(R.id.toolbar_button_left);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_right_image = findViewById(R.id.toolbar_button_right);
        drop_down_image = findViewById(R.id.imageView_drop_down);

        switch (type) {
            case 0:
                textView_title.setBackground(ApplicationController.setUpDrawable(R.drawable.image_logo));
                break;
            case 1:
            case 2:
            default:
                bottomSheet = findViewById(R.id.design_bottom_sheet);
                behavior = BottomSheetBehavior.from(bottomSheet);
                setLocation(0);
                drop_down_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_arrow_drop_down_black_24dp));
                drop_down_image.setVisibility(View.VISIBLE);
                drop_down_image.setOnClickListener(this);
                textView_title.setOnClickListener(this);
                break;
        }

        textView_title.setVisibility(View.VISIBLE);
        toolbar_left_image.setVisibility(View.VISIBLE);
        toolbar_left_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_account_circle_black_36dp));
        toolbar_left_image.setOnClickListener(this);
        toolbar_right_image.setVisibility(View.VISIBLE);
        toolbar_right_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_place_black_36dp));
        toolbar_right_image.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == toolbar_right_image) {
            intent = new Intent(this, RestaurantMapActivity.class);
            intent.putExtra("restaurants", restaurantList);
            ApplicationController.startActivity(this, intent);
        } else if (v == toolbar_left_image) {
            ApplicationController.finish(this);
        } else if (v == textView_title) {
            myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
        } else if (v == drop_down_image) {
            myBottomSheet.show(getSupportFragmentManager(), myBottomSheet.getTag());
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
//        ApplicationController.finish(this);
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onTabChanged(String tabId) {
        Log.d("adsasddsa", tabId);
    }

    private void setUpTabView() {
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);

        CustomPagerAdapter viewPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        ArrayList<ParcelableRestaurant> restaurants;
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList("restaurants", restaurantList);
        fragment.setArguments(bundle);

        viewPagerAdapter.addFragment(fragment, kindList.get(0));

        for (int i = 1; i < kindList.size(); i++) {
            fragment = new RecyclerViewFragment();
            bundle = new Bundle();
            restaurants = new ArrayList<>();

            for (ParcelableRestaurant restaurant : restaurantList) {
                if (restaurant.getCategory() == i) {
                    restaurants.add(new ParcelableRestaurant(restaurant));
                }
            }

            bundle.putParcelableArrayList("restaurants", restaurants);
            fragment.setArguments(bundle);

            viewPagerAdapter.addFragment(fragment, kindList.get(i));
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setLocation(int position) {
        textView_title.setText(locationList.get(position));
    }


}
