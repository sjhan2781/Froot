package com.example.hansangjin.froot.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
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

import com.example.hansangjin.froot.Adapter.RestaurantMapRecyclerViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Listener.MapMarkerClickListener;
import com.example.hansangjin.froot.Listener.ScrollListener;
import com.example.hansangjin.froot.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class RestaurantMapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener, Runnable {
    private final int MAP_ACTIVITY_CODE = 300;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private int permissionCheck;
    private boolean mLocationPermissionGranted;

    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location myLocation;

    private int selected_index;
    private RecyclerView recyclerView;
    private RestaurantMapRecyclerViewAdapter recyclerViewAdapter;

    private ArrayList<Restaurant> restaurantList;
    private ArrayList<Marker> markers;

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;

    private LinearLayoutManager linearLayoutManager;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        getLocationPermission();

        createObject();
        setUpToolbar();
        setUpData();
        setUpGoogleApiClient();
        setUpListener();
        setUpUI();

    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_not_move, R.anim.activity_right_out);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                    updateLocationUI();
                    getDeviceLocation();
                }
            }
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
        markers = new ArrayList<>();
        recyclerViewAdapter = new RestaurantMapRecyclerViewAdapter(restaurantList, this);

        NAVER_CLIENT_ID = getResources().getString(R.string.naver_api_client_key);
        NAVER_CLIENT_SECRET = getResources().getString(R.string.naver_api_client_secret);


    }

    private void setUpGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this /* FragmentActivity */,
                            this /* OnConnectionFailedListener */)
                    .addConnectionCallbacks(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        createLocationRequest();

        mGoogleApiClient.connect();
    }

    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar_start_image.setVisibility(View.VISIBLE);
        toolbar_start_image.setImageBitmap(ApplicationController.setUpImage(R.drawable.ic_clear_black_36dp));

        Shader textShader=new LinearGradient(0, 0, 0, 100,
                new int[]{Color.GREEN,Color.BLUE},
                new float[]{0, 1}, Shader.TileMode.CLAMP);

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText("주변 식당 찾기");
        textView_title.getPaint().setShader(textShader);

//        textView_title.setTextColor(getResources().getColor(R.color.logoColor));
    }

    private void setUpData() {
        restaurantList.add(new Restaurant(0, "게코스 테라스"));
        restaurantList.add(new Restaurant(1, "이태원 구월당"));
        restaurantList.add(new Restaurant(2, "홍대개미"));

        //네이버 식당 정보 검색
        try {
            Thread networkThread = new Thread(this);

            networkThread.start();
            networkThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void setUpUI() {
        int pixel = (int) (16 * ApplicationController.metrics.density);

        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                super.getItemOffsets(outRect, view, parent, state);
                outRect.left = 32;
                outRect.right = 32;
            }
        });


        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        recyclerView.setOnFlingListener(new RestaurantRecyclerViewFlingListener(this, recyclerView));
        recyclerView.addOnScrollListener(new ScrollListener(this, recyclerView.computeHorizontalScrollExtent()));

        recyclerView.setNestedScrollingEnabled(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);


        recyclerView.setPadding(pixel * 2, 0, pixel * 2, 0);
        recyclerView.setClipToPadding(false);   //양쪽에 아이템 살짝 보이게
    }

    private void setUpListener() {
        recyclerViewAdapter.setItemClick(new RestaurantMapRecyclerViewAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                intent.putExtra("restaurant", restaurantList.get(position));
                startActivityForResult(intent, MAP_ACTIVITY_CODE);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_not_move);
            }
        });
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    public void setSelectedChild(int position) {
        if(markers.size() != 0) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(markers.get(position).getPosition()));
            setMarkers(position);
        }
    }

    private void initMarkers() {
        Marker marker;
        MarkerOptions markerOptions;

        for (Restaurant restaurant : restaurantList) {
            LatLng latLng = new LatLng(restaurant.getMapy(), restaurant.getMapx());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.button_facebook_login_9)));

            marker = googleMap.addMarker(markerOptions);
            marker.setTag(restaurant);

            markers.add(marker);
        }

        selected_index = 0;
        markers.get(selected_index).setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.button_kakao_login_11)));
    }

    public void setMarkers(int position) {
        if(position != selected_index) {
            Marker marker = markers.get(selected_index);

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.button_facebook_login_9)));
            marker.setZIndex(1.0f);

            selected_index = position;
            marker = markers.get(selected_index);

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.button_kakao_login_11)));
            marker.setZIndex(2.0f);
        }
    }

    public void animateCamera(int position){
        if(position != selected_index) {
            Marker marker = markers.get(position);

            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            setMarkers(position);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        googleMap.setOnMarkerClickListener(new MapMarkerClickListener(this));

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();
    }

    private void updateLocationUI() {
        if (googleMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

            } else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                myLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }

    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @SuppressLint("MissingPermission")
    private void getDeviceLocation() {
        if (mLocationPermissionGranted) {
            myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        }

        // Set the map's camera position to the current location of the device.
        if (mCameraPosition != null) {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
        } else if (myLocation != null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                    new LatLng(myLocation.getLatitude(),
                            myLocation.getLongitude()), 10));


            for (Restaurant restaurant : restaurantList) {
                float distance;
                Location restaurantLocation = new Location(restaurant.getName());
                restaurantLocation.setLatitude(restaurant.getMapy());
                restaurantLocation.setLongitude(restaurant.getMapx());
                distance = myLocation.distanceTo(restaurantLocation);

                restaurant.setDistance(distance);
            }
            Collections.sort(restaurantList);

            recyclerViewAdapter.notifyDataSetChanged();

        } else {
            Log.d("aaaaaaaaaaa", "Current location is null. Using defaults.");
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.56, 126.97), 10));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        initMarkers();

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        myLocation = location;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void run() {
        //네이버 검색 네트워킹을 위한 쓰레드
        getSearchResult();
        convertCoor();

        return;
    }


}
