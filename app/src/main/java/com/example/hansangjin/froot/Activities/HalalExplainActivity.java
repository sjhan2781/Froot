package com.example.hansangjin.froot.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.R;

public class HalalExplainActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView toolbar_left_image;
    private TextView textView_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halal_explain);

        init();
    }

    private void init() {
        creatObjects();
        setUpUI();
    }

    private void creatObjects() {

    }


    private void setUpUI() {
        setUpToolbar();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_left_image = findViewById(R.id.toolbar_button_left);
        textView_title = findViewById(R.id.toolbar_textView_title);

        textView_title.setText("무슬림 친화 레스토랑");
        textView_title.setVisibility(View.VISIBLE);

        toolbar_left_image.setVisibility(View.VISIBLE);
        toolbar_left_image.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp_w);
        toolbar_left_image.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == toolbar_left_image){
            onBackPressed();
        }
    }

    @Override
    public void onBackPressed() {
        ApplicationController.finish(this);
    }
}
