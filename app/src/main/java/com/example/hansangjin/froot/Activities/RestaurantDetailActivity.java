package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Adapter.FoodRecyclerViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Food;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class RestaurantDetailActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView toolbar_left_image, toolbar_right_image;
    private TextView textView_title;

    private TextView textView_restaurant_name, textView_restaurant_address, textView_restaurant_type, textView_available_count;
    private  ImageView imageView_restaurant;

    private RecyclerView recyclerView_food;
    private FoodRecyclerViewAdapter foodRecyclerViewAdapter;
    private String foods;
    private ArrayList<Food> foodList;

    private ParcelableRestaurant restaurant;
    private Intent intent;

    private Locale locale;
    private String locale_str;

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
        ApplicationController.finish(this);
    }

    private void createObjects(){
        foodList = new ArrayList<>();

        recyclerView_food = findViewById(R.id.recyclerView_food);

        textView_restaurant_name = findViewById(R.id.textView_restaurant_name);
        textView_restaurant_address = findViewById(R.id.textView_restaurant_address);
        textView_restaurant_type = findViewById(R.id.textView_restaurant_category);
        textView_available_count = findViewById(R.id.textView_available_count);

        imageView_restaurant = findViewById(R.id.imageView_restaurant);

        foodRecyclerViewAdapter = new FoodRecyclerViewAdapter(this, foodList);
        recyclerView_food.setAdapter(foodRecyclerViewAdapter);

        intent = getIntent();

        restaurant = intent.getParcelableExtra("restaurant");
        foods = restaurant.getFoods();
        Log.d("ddddd", foods);

        locale = getResources().getConfiguration().locale;
        Log.d("language", locale.getLanguage());

        if (locale.getLanguage().equals("ko")){
            locale_str = "ko";
        }
        else if (locale.getLanguage().equals("ja")){
            locale_str = "ja";
        }
        else if (locale.getLanguage().equals("rCN")){
            locale_str = "rCN";
        }
        else if (locale.getLanguage().equals("rTW")){
            locale_str = "rTW";
        }
        else {
            locale_str = "en";
        }
    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_left_image = findViewById(R.id.toolbar_button_left);
        toolbar_right_image = findViewById(R.id.toolbar_button_right);
        textView_title = findViewById(R.id.toolbar_textView_title);


        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText(restaurant.getName());

        toolbar_left_image.setVisibility(View.VISIBLE);
        toolbar_left_image.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp_w);
        toolbar_left_image.setOnClickListener(this);

    }

    private void setUpData(){
        getFoodData("http://froot.iptime.org:8080/restaurantDetail_activity.php");
    }

    private void setUpUI(){
        int image_width = imageView_restaurant.getLayoutParams().width;
        int image_height = imageView_restaurant.getLayoutParams().height;


        textView_restaurant_name.setText(restaurant.getName());

        if(!ApplicationController.getRestaurantTypes().isEmpty()) {
            textView_restaurant_type.setText(ApplicationController.getRestaurantTypes().get(restaurant.getCategory()).getType());
        }

        textView_restaurant_address.setText(restaurant.getAddress());
        textView_available_count.setText(String.format("%s개의 적합식품", restaurant.getFoods().split(",").length));

        if(!restaurant.getImage_base64().isEmpty()) {
            imageView_restaurant.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(restaurant.getImage_base64()), image_width, image_height, true));
        }

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

    @Override
    public void onClick(View v) {
        if(v == toolbar_left_image){
            onBackPressed();
        }
    }

    public void getFoodData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    con.setReadTimeout(5000);
                    con.setConnectTimeout(10000);
                    con.setUseCaches(false); // 캐시 사용 안 함
                    con.setRequestMethod("POST");
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    OutputStream os = con.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8")); //캐릭터셋 설정
                    writer.write("lang=" + locale_str + "&restaurant=" + restaurant.getID() + "&foods=" + foods); //요청 파라미터를 입력
                    writer.flush();
                    writer.close();
                    os.close();

                    con.connect();

                    int retCode = con.getResponseCode();

                    Log.d("ResponseCode", retCode + "");

                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String result) {
                Log.d("result", result);
                setFoodList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    private void setFoodList(String strJSON){
        try {
            JSONObject json = new JSONObject(strJSON);

            JSONObject jsonObj = json.getJSONObject("food");
            JSONArray jsonArray;

            jsonArray = jsonObj.getJSONArray("available");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                int id = c.getInt("food_id");
                String food_str = c.getString("food_str");
                int price = c.getInt("price");
                String image_base64 = c.getString("image_base64");

                foodList.add(new Food(id, food_str, price, image_base64, true));
            }

            jsonArray = jsonObj.getJSONArray("unavailable");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                int id = c.getInt("food_id");
                String food_str = c.getString("food_str");
                int price = c.getInt("price");
                String image_base64 = c.getString("image_base64");

                foodList.add(new Food(id, food_str, price, image_base64, false));
            }

            Collections.sort(foodList);

            foodRecyclerViewAdapter.notifyDataSetChanged();

        } catch (Exception e) {

        }
    }

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
