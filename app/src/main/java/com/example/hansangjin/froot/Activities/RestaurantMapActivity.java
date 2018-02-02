package com.example.hansangjin.froot.Activities;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Fragment.MapFragment;
import com.example.hansangjin.froot.R;
import com.nhn.android.maps.NMapView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class RestaurantMapActivity extends AppCompatActivity implements Runnable {
    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private MapFragment mapFragment;

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<Restaurant> restaurantList;

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;

    private NMapView mMapView;

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

        mMapView = findViewById(R.id.mapView);

        restaurantList = new ArrayList<>();
        recyclerViewAdapter = new RecyclerViewAdapter();


        NAVER_CLIENT_ID = getResources().getString(R.string.naver_api_client_key);
        NAVER_CLIENT_SECRET = getResources().getString(R.string.naver_api_client_secret);


    }

    private void setUpToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_start_image.setVisibility(View.VISIBLE);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_clear_black_36dp));

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText("주변 식당 찾기");
//        toolbar_end_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.button_exit_2));
    }

    private void setUpData(){
        restaurantList.add(new Restaurant("게코스 테라스"));
        restaurantList.add(new Restaurant("구월당"));
        restaurantList.add(new Restaurant("홍대개미"));

        new Thread(this).start();

    }

    private void setUpNaverMap(){
        mapFragment = new MapFragment();
        mapFragment.setArguments(new Bundle());

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.add(R.id.naver_map_fragment, mapFragment);



        fragmentTransaction.commit();
    }

    private void setUpUI(){
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.right = 15;
            }
        });

        recyclerView.setAdapter(recyclerViewAdapter);

    }

    //식당 검색
    private void getSearchResult(){
        for(int i = 0; i < restaurantList.size(); i++) {
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
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                } else {
                    br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                }
                sb = new StringBuilder();
                String line;

                while ((line = br.readLine()) != null) {
                    sb.append(line + "\n");
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

    private void setRestaurantInfo(StringBuilder sb, int position){
        String data = sb.toString();
        String[] array;
        array = data.split("\"");

        for (int i = 0; i < array.length; i++) {
            //arry[i+2]가 json에서 내가 원하는 데이터
            switch (array[i].toString()){
                case "title" :
//                    restaurantList.get(position).setName(array[i+2]);
                    break;
                case "link" :
                    restaurantList.get(position).setLink(array[i+2]);
                    break;
                case "category" :
                    restaurantList.get(position).setCategory(array[i+2]);
                    break;
                case "description" :
                    restaurantList.get(position).setDescription(array[i+2]);
                    break;
                case "telephone" :
                    restaurantList.get(position).setTelephone(array[i+2]);
                    break;
                case "address" :
                    restaurantList.get(position).setAddress(array[i+2]);
                    break;
                case "mapx" :
                    restaurantList.get(position).setMapx(array[i+2]);
                    break;
                case "mapy" :
                    restaurantList.get(position).setMapy(array[i+2]);
                    break;
            }
        }
    }

    @Override
    public void run() {
        //네이버 검색 네트워킹을 위한 쓰레드
        getSearchResult();

//        Log.d("Restaurants", restaurantList.toString());

        return;
    }

    class RecyclerViewAdapter extends RecyclerView.Adapter<MyViewHolder> {
        private int selectedPos = 0;

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_resaurant_map, parent, false);

            return new MyViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final int curPos = position;

            holder.textView_restaurant_name.setText(restaurantList.get(position).getName());
            holder.textView_restaurant_category.setText(restaurantList.get(position).getCategory());
            holder.textView_restaurant_phonenumber.setText(restaurantList.get(position).getTelephone());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notifyItemChanged(selectedPos);
                    selectedPos = curPos;
//                    activity.setSelectedFood(foods.get(curPos));
                    notifyItemChanged(selectedPos);
                    Log.d("item", holder.textView_restaurant_name.getText().toString());
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

        private TextView textView_restaurant_name, textView_restaurant_category, textView_restaurant_phonenumber;
        private ImageView store_image;


        public MyViewHolder(View parent) {
            super(parent);

            this.textView_restaurant_name = parent.findViewById(R.id.textView_restaurant_name);
            this.textView_restaurant_category = parent.findViewById(R.id.textView_restaurant_category);
            this.textView_restaurant_phonenumber = parent.findViewById(R.id.textView_restaurant_phonenumber);

        }
    }
}
