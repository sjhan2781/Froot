package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.Adapter.CustomPagerAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.BackPressCloseHandler;
import com.example.hansangjin.froot.CustomView.MyBottomSheetDialogFragment;
import com.example.hansangjin.froot.CustomView.NoDataDialog;
import com.example.hansangjin.froot.CustomView.RecyclerViewFragment;
import com.example.hansangjin.froot.Data.Location;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Data.RestaurantType;
import com.example.hansangjin.froot.ParcelableData.ParcelableLocation;
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

public class RestaurantListActivity extends AppCompatActivity implements View.OnClickListener, TabHost.OnTabChangeListener {
    private ImageView toolbar_left_image, toolbar_right_image, drop_down_image;
    private TextView textView_title;

    private Intent intent;
    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<ParcelableRestaurantType> restaurantTypeList;
    private ArrayList<ParcelableLocation> locationList;

    private View bottomSheet;
    private BottomSheetBehavior behavior;
    private BottomSheetDialogFragment myBottomSheet;

    private BackPressCloseHandler backPressCloseHandler;

    private Toast toast;

    private Locale locale;
    private String locale_str;

    private String selected_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        init();
    }

    private void init() {
        creatObjects();
        setUpData();
    }

    private void creatObjects() {
        intent = getIntent();
        selected_info = intent.getStringExtra("selected_info");

        selected_info = selected_info.substring(1, selected_info.length()-1);

        Log.d("asdasd", selected_info);

        toolbar_left_image = findViewById(R.id.toolbar_button_left);
        toolbar_right_image = findViewById(R.id.toolbar_button_right);
        bottomSheet = findViewById(R.id.design_bottom_sheet);
        textView_title = findViewById(R.id.toolbar_textView_title);
        drop_down_image = findViewById(R.id.imageView_drop_down);

        restaurantList = new ArrayList<>();
        restaurantTypeList = new ArrayList<>();
        locationList = new ArrayList<>();

        myBottomSheet = MyBottomSheetDialogFragment.newInstance(locationList);

        backPressCloseHandler = new BackPressCloseHandler(this);

        locale = getResources().getConfiguration().locale;

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
        getLocationData("http://" + ApplicationController.getServerIP() + "/getLocation.php");

    }

    private void setUpUI() {
        setUpToolbar();
        setUpToast();
        setUpTabView();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        behavior = BottomSheetBehavior.from(bottomSheet);

        drop_down_image.setVisibility(View.VISIBLE);
        textView_title.setVisibility(View.VISIBLE);

        drop_down_image.setOnClickListener(this);
        textView_title.setOnClickListener(this);

        toolbar_left_image.setVisibility(View.VISIBLE);
        toolbar_left_image.setImageResource(R.drawable.ic_account_circle_black_36dp);
        toolbar_left_image.setOnClickListener(this);
        toolbar_right_image.setVisibility(View.VISIBLE);
        toolbar_right_image.setImageResource(R.drawable.ic_place_black_36dp);
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
        backPressCloseHandler.onBackPressed();
    }

    @Override
    public void onTabChanged(String tabId) {

    }

    private void setUpTabView() {
        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.view_pager);

        CustomPagerAdapter viewPagerAdapter = new CustomPagerAdapter(getSupportFragmentManager());

        ArrayList<ParcelableRestaurant> restaurants;
        RecyclerViewFragment fragment = new RecyclerViewFragment();
        Bundle bundle = new Bundle();

        restaurantTypeList.get(0).setEnabled(true);

        for (ParcelableRestaurant restaurant : restaurantList) {
            restaurantTypeList.get(restaurant.getCategory()).setEnabled(true);
        }


        bundle.putParcelableArrayList("restaurants", restaurantList);
        bundle.putParcelableArrayList("restaurantTypes", restaurantTypeList);
        fragment.setArguments(bundle);

        viewPagerAdapter.addFragment(fragment, restaurantTypeList.get(0).getType());

        for (int i = 1; i < restaurantTypeList.size(); i++) {
            if(!restaurantTypeList.get(i).isEnabled()){
                continue;
            }

            fragment = new RecyclerViewFragment();
            bundle = new Bundle();
            restaurants = new ArrayList<>();

            for (ParcelableRestaurant restaurant : restaurantList) {
                if (restaurant.getCategory() == i) {
                    restaurants.add(new ParcelableRestaurant(restaurant));
                }
            }

            bundle.putParcelableArrayList("restaurants", restaurants);
            bundle.putParcelableArrayList("restaurantTypes", restaurantTypeList);
            fragment.setArguments(bundle);

            viewPagerAdapter.addFragment(fragment, restaurantTypeList.get(i).getType());
        }

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void setLocation(int position) {
        textView_title.setText(locationList.get(position).getLocation());
        getRestaurantData("http://" + ApplicationController.getServerIP() + "/restaurantList_activity.php", locationList.get(position));
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

    public void getLocationData(String url){
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
                setLocationList(result);
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public void getRestaurantData(String url, final Location curLocation) {
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
                    Log.d("Aaaa", "lang=" + locale_str + "&location=" + curLocation.getId() + "&select=" + selected_info); //요청 파라미터를 입력

                    writer.write("lang=" + locale_str + "&location=" + curLocation.getId() + "&select=" + selected_info); //요청 파라미터를 입력
//                    writer.write("lang=" + locale_str); //요청 파라미터를 입력

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

    private void setLocationList(String strJSON){
        try {
            JSONObject jsonObj = new JSONObject(strJSON);

            JSONArray jsonArray = jsonObj.getJSONArray("location");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                int id = c.getInt("location_id");
                String location_str = c.getString("location_str");

                locationList.add(new ParcelableLocation(new Location(id, location_str)));
            }


            jsonArray = jsonObj.getJSONArray("restaurant_type");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);
                int id = c.getInt("type_id");
                String category_str = c.getString("type_str");

                restaurantTypeList.add(new ParcelableRestaurantType(new RestaurantType(id, category_str)));
            }


            setLocation(0);

        } catch (Exception e) {

        }
    }

    private void setRestaurantList(String strJSON) {
        try {
            restaurantList.clear();

            JSONObject jsonObj = new JSONObject(strJSON);

            JSONArray jsonArray = null;

            jsonArray = jsonObj.getJSONArray("restaurant");

            Log.d("restaurantList", strJSON);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject c = jsonArray.getJSONObject(i);

                Restaurant restaurant = new Restaurant();
                restaurant.setID(c.getInt("restaurant_id"));
                restaurant.setName(c.getString("restaurant_str"));
                restaurant.setCategory(c.getInt("category"));
                restaurant.setAddress(c.getString("address"));
                restaurant.setTelephone(c.getString("telephone"));
                restaurant.setMapx(c.getDouble("lat"));
                restaurant.setMapy( c.getDouble("lng"));
                restaurant.setHalal(c.getInt("halal"));
                restaurant.setImage_base64(c.getString("image_base64"));
                restaurant.setFoods(c.getString("foods"));

                restaurantList.add(new ParcelableRestaurant(restaurant));
            }

            if(restaurantList.isEmpty()){
                NoDataDialog noDataDialog = new NoDataDialog(this);
                noDataDialog.show();
            }
            else{
                setUpUI();
            }

        } catch (Exception e) {

        }
    }

}
