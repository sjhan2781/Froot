package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.Adapter.CategoryExpandableListViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.CustomView.GradientTextView;
import com.example.hansangjin.froot.Data.Category;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private Button register_button;
    private ImageView exit_button;
    private GradientTextView textView_title;

    private ExpandableListView categoryListView;
    private CategoryExpandableListViewAdapter categoryExpandableListViewAdapter;

    private ArrayList<Category> categories;

    private ArrayList<String> religionList = new ArrayList<>();
    private ArrayList<String> allergyList = new ArrayList<>();
    private ArrayList<String> vegetarianList = new ArrayList<>();

    private static Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
    }

    private void init() {
        createObjects();
        setUpToolbar();
        setUpData();
        setUpUI();
        setUpListenter();
        setUpToast();
    }

    private void createObjects() {
        register_button = findViewById(R.id.button_register);

        textView_title = findViewById(R.id.toolbar_textView_title);
        exit_button = findViewById(R.id.toolbar_button_left);

        categoryListView = findViewById(R.id.expandableListView);
        categories = new ArrayList<>();

        religionList = new ArrayList<>();
        vegetarianList = new ArrayList<>();
        allergyList = new ArrayList<>();

        categoryExpandableListViewAdapter = new CategoryExpandableListViewAdapter(this, categoryListView, categories);
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText("식이유형");

        exit_button.setVisibility(View.VISIBLE);
        exit_button.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_clear_black_36dp));
    }

    private void setUpData() {
        religionList.add("무슬림");

        vegetarianList.add("생활비건");
        vegetarianList.add("비건");
        vegetarianList.add("오보");
        vegetarianList.add("락토");
        vegetarianList.add("락토오보");

        allergyList.add("d");
        allergyList.add("e");

        categories.add(new Category("종교", 0, religionList));
        categories.add(new Category("채식주의", 1, vegetarianList));
        categories.add(new Category("알러지", 2, allergyList));

    }

    private void setUpUI() {
        register_button.setEnabled(false);
//        categoryExpandableListViewAdapter.notifyDataSetChanged();
        categoryListView.setAdapter(categoryExpandableListViewAdapter);

        categoryListView.setOnGroupExpandListener(categoryExpandableListViewAdapter);

//        register_button.setBackground(ApplicationController.setUpDrawable(R.drawable.button_recommend_restaurant));

    }

    private void setUpListenter() {
        exit_button.setOnClickListener(this);

        register_button.setOnClickListener(this);
    }

//

    private void setUpToast() {
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.toast, (ViewGroup) findViewById(R.id.layout_toast));
        TextView textView = view.findViewById(R.id.textView_toast);
        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        textView.setText("adsadadasd");
        toast.setView(view);
    }

    @Override
    public void onClick(View v) {
        if (v == exit_button) {
            Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            ApplicationController.startActivity(this, intent);
        } else if (v == register_button) {
            Intent intent = new Intent(getApplicationContext(), RestaurantListActivity.class);
            intent.putExtra("category_type",  categoryExpandableListViewAdapter.getSelectedCategory().getType());
            ApplicationController.startActivity(this, intent);
        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
        }

    }

    public void setRegisterEnable(boolean isEnable){
        register_button.setEnabled(isEnable);
//        register_button.setSelected(isEnable);
    }
}
