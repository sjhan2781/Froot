package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Listener.AllergyCheckListener;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;


public class CategoryActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {
    private ArrayList<ToggleButton> toggleButtons = new ArrayList<>();
    private ArrayList<RadioGroup> toggleViews = new ArrayList<>();
    private ToggleButton toggle_religion, toggle_vegetarian, toggle_allergy;
    private RadioGroup radiogroup_religion, radiogroup_vegetarian, radiogroup_allergy;
    private Button register_button;
    private ImageView exit_button;
    private TextView textView_title;

    private ArrayList<String> religionList = new ArrayList<>();
    private ArrayList<String> allergyList = new ArrayList<>();
    private ArrayList<String> vegetarianList = new ArrayList<>();

    private AllergyCheckListener allergyCheckListener;

    private static Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        init();
    }

    private void createObjects() {
        toggle_allergy = findViewById(R.id.toggle_allergy);
        toggle_religion = findViewById(R.id.toggle_religion);
        toggle_vegetarian = findViewById(R.id.toggle_vegetarian);

        radiogroup_religion = findViewById(R.id.radio_group_religion);
        radiogroup_vegetarian = findViewById(R.id.radio_group_vegetarian);
        radiogroup_allergy = findViewById(R.id.radio_group_allergy);

        register_button = findViewById(R.id.button_register);

        textView_title = findViewById(R.id.toolbar_textView_title);
        exit_button = findViewById(R.id.toolbar_button_second);


        toggleButtons = new ArrayList<>();
        toggleViews = new ArrayList<>();

        religionList = new ArrayList<>();
        vegetarianList = new ArrayList<>();
        allergyList = new ArrayList<>();

        allergyCheckListener = new AllergyCheckListener();

    }

    private void init() {
        createObjects();
        setUpToolbar();
        setUpData();
        setUpUI();
        setUpListenter();
        setUpToast();
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

        toggleButtons.add(toggle_religion);
        toggleButtons.add(toggle_vegetarian);
        toggleButtons.add(toggle_allergy);

        toggleViews.add(radiogroup_religion);
        toggleViews.add(radiogroup_vegetarian);
        toggleViews.add(radiogroup_allergy);
    }

    private void setUpUI() {
        setUpRadioButtons();

//        register_button.setBackground(ApplicationController.setUpDrawable(R.drawable.button_recommend_restaurant));

        for (int i = 0; i < toggleButtons.size(); i++) {
//            toggleButtons.get(i).setBackground(ApplicationController.setUpDrawable(R.drawable.category_box_selector));
        }
    }

    private void setUpListenter() {
        exit_button.setOnClickListener(this);

        toggle_religion.setOnClickListener(this);
        toggle_vegetarian.setOnClickListener(this);
        toggle_allergy.setOnClickListener(this);
        register_button.setOnClickListener(this);

        toggle_religion.setOnCheckedChangeListener(this);
        toggle_vegetarian.setOnCheckedChangeListener(this);
        toggle_allergy.setOnCheckedChangeListener(this);

        register_button.setOnClickListener(this);
    }

    private void setUpRadioButtons() {
        for (int i = 0; i < religionList.size(); i++) {
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setId(i + 1);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(religionList.get(i));
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setVisibility(View.VISIBLE);

            radiogroup_religion.addView(radioButton);
        }

        for (int i = 0; i < vegetarianList.size(); i++) {
            RadioButton radioButton = new RadioButton(getApplicationContext());
            radioButton.setId(i + 1);
            radioButton.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            radioButton.setText(vegetarianList.get(i));
            radioButton.setTextColor(getResources().getColor(R.color.black));
            radioButton.setHighlightColor(getResources().getColor(R.color.black));
            radioButton.setVisibility(View.VISIBLE);

            radiogroup_vegetarian.addView(radioButton);
        }

        for (int i = 0; i < allergyList.size(); i++) {
            CheckBox checkBox = new CheckBox(getApplicationContext());
            checkBox.setId(i + 1);
            checkBox.setLayoutParams(new RadioGroup.LayoutParams(RadioGroup.LayoutParams.MATCH_PARENT, RadioGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setText(allergyList.get(i));
            checkBox.setOnCheckedChangeListener(allergyCheckListener);
            checkBox.setTextColor(getResources().getColor(R.color.black));
            checkBox.setVisibility(View.VISIBLE);

            radiogroup_allergy.addView(checkBox);
        }
    }

    private void setUpToast(){
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
            finish();
        } else if (v == toggle_religion) {
            toggle_religion.setSelected(true);
            toggle_religion.setChecked(true);

        } else if (v == toggle_vegetarian) {
            toggle_vegetarian.setSelected(true);
            toggle_vegetarian.setChecked(true);

        } else if (v == toggle_allergy) {
            toast.show();

            toggle_allergy.setSelected(true);
            toggle_allergy.setChecked(true);
        } else if (v == register_button) {
            if (radiogroup_religion.getVisibility() == View.VISIBLE) {
                Log.d("isChecked", radiogroup_religion.getCheckedRadioButtonId() + "");
            } else if (radiogroup_vegetarian.getVisibility() == View.VISIBLE) {
                Log.d("isChecked", radiogroup_vegetarian.getCheckedRadioButtonId() + "");
            } else if (radiogroup_allergy.getVisibility() == View.VISIBLE) {
                Log.d("isChecked", allergyCheckListener.getSelectedItem().toString());
            }

            startActivity(new Intent(getApplicationContext(), RestaurantMapActivity.class));

        }

    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            for (ToggleButton button : toggleButtons) {
                if (button != buttonView) {
                    button.setChecked(false);
                }
            }

        }

        for (int i = 0; i < toggleButtons.size(); i++) {
            if (toggleButtons.get(i).isChecked()) {
                toggleButtons.get(i).setTextColor(Color.parseColor("#ffffff"));
                toggleViews.get(i).setVisibility(View.VISIBLE);
            } else {
                toggleButtons.get(i).setTextColor(Color.parseColor("#000000"));
                toggleViews.get(i).setVisibility(View.GONE);
            }
        }


    }
}
