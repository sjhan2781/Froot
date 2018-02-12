package com.example.hansangjin.froot.Activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.Adapter.RestaurantMapRecyclerViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Fragment.MapFragment;
import com.example.hansangjin.froot.Listener.RestaurantRecyclerViewFlingListener;
import com.example.hansangjin.froot.R;
import com.nhn.android.maps.maplib.NGeoPoint;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class RestaurantMapActivity extends AppCompatActivity implements Runnable {
    private final int MAP_ACTIVITY_CODE = 300;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private int permissionCheck;

    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private MapFragment mapFragment;

    private RecyclerView recyclerView;
    private RestaurantMapRecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<Restaurant> restaurantList;

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;

    private LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= 23 && permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
            // 권한 없음
        }
        createObject();
        setUpToolbar();
        setUpData();
        setUpNaverMap();
        setUpListener();
        setUpUI();
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpMapOverlay();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            setRestaurantDistance();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // 갤러리 사용권한에 대한 콜백을 받음
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 권한 동의 버튼 선택
                    setRestaurantDistance();
                    recyclerView.invalidate();
                } else {
                    // 사용자가 권한 동의를 안함
                    // 권한 동의안함 버튼 선택
                    Toast.makeText(RestaurantMapActivity.this, "권한사용을 동의해주셔야 현재 위치 이용이 가능합니다.", Toast.LENGTH_SHORT).show();
                }

                break;
            }
            // 예외케이스
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void createObject() {
        recyclerView = findViewById(R.id.restaurant_list_view);
        toolbar_start_image = findViewById(R.id.toolbar_button_first);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_second);

        linearLayoutManager = new LinearLayoutManager(this);

        restaurantList = new ArrayList<>();
        recyclerViewAdapter = new RestaurantMapRecyclerViewAdapter(restaurantList, this);

        NAVER_CLIENT_ID = getResources().getString(R.string.naver_api_client_key);
        NAVER_CLIENT_SECRET = getResources().getString(R.string.naver_api_client_secret);

    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_start_image.setVisibility(View.VISIBLE);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_clear_black_36dp));

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText("주변 식당 찾기");
    }

    private void setUpData() {
        restaurantList.add(new Restaurant("게코스 테라스"));
        restaurantList.add(new Restaurant("이태원 구월당"));
        restaurantList.add(new Restaurant("홍대개미"));

        //네이버 식당 정보 검색
        try {
            Thread networkThread = new Thread(this);

            networkThread.start();
            networkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpNaverMap() {
        mapFragment = new MapFragment();
        mapFragment.setArguments(new Bundle());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.naver_map_fragment, mapFragment);

        fragmentTransaction.commit();
    }

    private void setUpUI() {
        int pixel = (int) (16 * ApplicationController.metrics.density);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 8;
                outRect.right = 8;
            }
        });


        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        recyclerView.setOnFlingListener(new RestaurantRecyclerViewFlingListener(this, recyclerView));

        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

        Collections.sort(restaurantList);

        recyclerView.setPadding(pixel, 0, pixel, 0);
        recyclerView.setClipToPadding(false);   //양쪽에 아이템 살짝 보이게
    }

    private void setUpListener() {
        recyclerViewAdapter.setItemClick(new RestaurantMapRecyclerViewAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                intent.putExtra("restaurant", restaurantList.get(position));
                startActivityForResult(intent, MAP_ACTIVITY_CODE);
            }
        });
    }

    private void setUpMapOverlay(){
        mapFragment.setRestaurants(restaurantList);
        mapFragment.initMarker();
//        setMarker(0);
    }

    //식당 검색
    private void getSearchResult() {
        for (int i = 0; i < restaurantList.size(); i++) {
            StringBuilder sb = null;
            String keyword = restaurantList.get(i).getName();

            try {
                String text = URLEncoder.encode(keyword, "UTF8");
                String urlStr = "https://openapi.naver.com/v1/search/local.json?query=" + text;
                URL url = null;
                url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-ID", NAVER_CLIENT_ID); //발급받은ID
                connection.setRequestProperty("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);//발급받은PW

                int responseCode = connection.getResponseCode();
                BufferedReader br;
                if (responseCode == connection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                connection.disconnect();

                setRestaurantInfo(sb, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.d("Restaurant", restaurantList.toString());
    }

    private void setRestaurantInfo(StringBuilder sb, int position) {
        try {
            JSONObject jsonObject = new JSONObject(sb.toString());

            JSONArray items = jsonObject.getJSONArray("items");

            for (int j = 0; j < items.length(); j++) {
                JSONObject value = items.getJSONObject(j);

                restaurantList.get(position).setName(Html.fromHtml(value.getString("title")).toString());
                restaurantList.get(position).setLink(value.getString("link"));
                restaurantList.get(position).setCategory(value.getString("category"));
                restaurantList.get(position).setDescription(value.getString("description"));
                restaurantList.get(position).setTelephone(value.getString("telephone"));
                restaurantList.get(position).setAddress(value.getString("address"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void convertCoor() {
        for (int i = 0; i < restaurantList.size(); i++) {
            try {
                String addr = URLEncoder.encode(restaurantList.get(i).getAddress(), "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/map/geocode?query=" + addr; //json
//                String apiURL = "https://openapi.naver.com/v1/map/geocode.xml?query=" + addr; // xml
                URL url = new URL(apiURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("X-Naver-Client-Id", NAVER_CLIENT_ID);
                connection.setRequestProperty("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);

                int responseCode = connection.getResponseCode();
                BufferedReader br;
                StringBuilder sb;

                if (responseCode == connection.HTTP_OK) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                br.close();
                connection.disconnect();


                setCoor(sb, i);

            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private void setCoor(StringBuilder sb, int position) {

        try {
            JSONObject result = new JSONObject(sb.toString()).getJSONObject("result");

            JSONArray items = result.getJSONArray("items");

            for (int j = 0; j < items.length(); j++) {
                JSONObject point = items.getJSONObject(j).getJSONObject("point");

                String x = point.getString("x");
                String y = point.getString("y");

                restaurantList.get(position).setMapx(Double.parseDouble(x));
                restaurantList.get(position).setMapy(Double.parseDouble(y));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setRestaurantDistance(){
        mapFragment.startLocation();

        NGeoPoint myLocation = mapFragment.getMyLocation();
//        myLocation = new NGeoPoint(127.108099, 37.366034);

        for(Restaurant restaurant : restaurantList) {
            NGeoPoint restaurantPoint = new NGeoPoint(restaurant.getMapx(), restaurant.getMapy());
//            double distance = NGeoPoint.getDistance(restaurantPoint, myLocation);
//
//            restaurant.setDistance(distance);
        }
    }

    public void setSelectedChild(int position) {
        recyclerView.smoothScrollToPosition(position);
    }

    public void setMarker(int position) {
        mapFragment.setMarker(position);
    }

    @Override
    public void run() {
        //네이버 검색 네트워킹을 위한 쓰레드
        getSearchResult();
        convertCoor();

        return;
    }


}
