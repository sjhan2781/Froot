package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.Adapter.RestaurantListRecycleViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.BackPressCloseHandler;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Data.RestaurantType;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurantType;
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
import java.util.Locale;

public class ReligionRestaurantActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView toolbar_left_image, toolbar_right_image, toolbar_image_logo;
    private RelativeLayout container_halal;

    private Intent intent;
    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<ParcelableRestaurantType> restaurantTypeList;

    private RecyclerView restaurantRecyclerView;
    private RestaurantListRecycleViewAdapter restaurantListRecycleViewAdapter;

    private BackPressCloseHandler backPressCloseHandler;

    private Toast toast;

    private Locale locale;
    private String locale_str;

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
        restaurantTypeList = new ArrayList<>();

        restaurantRecyclerView = findViewById(R.id.recyclerView_restaurant);

        restaurantListRecycleViewAdapter = new RestaurantListRecycleViewAdapter(this, restaurantList, restaurantTypeList);

        backPressCloseHandler = new BackPressCloseHandler(this);

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

    private void setUpData() {
        getRestaurantData("http://froot.iptime.org:8080/religionRestaurantList_activity.php");
    }

    private void setUpUI() {
        setUpToolbar();
        setUpToast();

        restaurantRecyclerView.setAdapter(restaurantListRecycleViewAdapter);
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

    public void getRestaurantData(String url) {
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
                    writer.write("lang=" + locale_str); //요청 파라미터를 입력
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
                setRestaurantList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    private void setRestaurantList(String strJSON) {
        try {
            restaurantList.clear();

            JSONObject jsonObj = new JSONObject(strJSON);

            JSONArray jsonArray = null;

            jsonArray = jsonObj.getJSONArray("restaurant_type");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                int id = c.getInt("type_id");
                String category_str = c.getString("type_str");

                restaurantTypeList.add(new ParcelableRestaurantType(new RestaurantType(id, category_str)));
            }

            ApplicationController.setRestaurantTypes(restaurantTypeList);

            jsonArray = jsonObj.getJSONArray("restaurant");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Restaurant restaurant = new Restaurant();
                restaurant.setID(c.getInt("restaurant_id"));
                restaurant.setName(c.getString("restaurant_str"));
                restaurant.setCategory(c.getInt("category"));
                restaurant.setAddress(c.getString("address"));
                restaurant.setTelephone(c.getString("telephone"));
                restaurant.setMapx(c.getInt("lat"));
                restaurant.setMapy( c.getInt("lng"));
                restaurant.setHalal(c.getInt("halal"));
                restaurant.setImage_base64(c.getString("image_base64"));
                restaurant.setFoods(c.getString("foods"));

                restaurantList.add(new ParcelableRestaurant(restaurant));
            }

            restaurantListRecycleViewAdapter.notifyDataSetChanged();
            restaurantRecyclerView.invalidate();

        } catch (Exception e) {

        }
    }
}
