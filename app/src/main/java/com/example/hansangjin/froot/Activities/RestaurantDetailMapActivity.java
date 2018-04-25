package com.example.hansangjin.froot.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.ParcelableData.ParcelableRestaurant;
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

public class RestaurantDetailMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {
    private final int MAP_ACTIVITY_CODE = 300;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private Intent intent;
    private boolean mLocationPermissionGranted;

    private ImageView toolbar_start_image, toolbar_end_image;
    private TextView textView_title;

    private TextView textView_restaurant_name, textView_restaurant_category,
            textView_restaurant_phonenumber, textView_restaurant_distance, textView_restaurant_address;
    private ImageView imageView_restaurant;

    private CardView cardView_restaurant;

    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private Location myLocation;

    private int selected_index;

    private ParcelableRestaurant restaurant;
    private Marker marker;


    private LinearLayoutManager linearLayoutManager;
    private CameraPosition mCameraPosition;

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail_map);

        ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION);
//        permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION);

        createObject();

        getLocationPermission();
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
        ApplicationController.finish(this);
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
        intent = getIntent();

        restaurant = intent.getParcelableExtra("restaurant");

        toolbar_start_image = findViewById(R.id.toolbar_button_left);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_right);

        imageView_restaurant = findViewById(R.id.imageView_restaurant);
        textView_restaurant_name = findViewById(R.id.textView_restaurant_name);
        textView_restaurant_category = findViewById(R.id.textView_restaurant_category);
        textView_restaurant_phonenumber = findViewById(R.id.textView_restaurant_phonenumber);
        textView_restaurant_distance = findViewById(R.id.textView_distance);
        textView_restaurant_address = findViewById(R.id.textView_review_address);

        cardView_restaurant = findViewById(R.id.cardView_restaurant);

        linearLayoutManager = new LinearLayoutManager(this);


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
        toolbar_start_image.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp_w);
        toolbar_start_image.setOnClickListener(this);

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText(restaurant.getName());
        textView_title.setTextColor(getResources().getColor(R.color.logoColor));
    }

    private void setUpData() {
        restaurant = intent.getParcelableExtra("restaurant");

    }

    private void setUpUI() {
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        int image_width = imageView_restaurant.getLayoutParams().width;
        int image_height = imageView_restaurant.getLayoutParams().height;


        textView_restaurant_name.setText(restaurant.getName());
        textView_restaurant_category.setText(ApplicationController.getRestaurantTypes().get(restaurant.getCategory()).getType());
        textView_restaurant_phonenumber.setText(Html.fromHtml("<u>" + restaurant.getTelephone() + "</u>"));
        textView_restaurant_distance.setText(String.valueOf(restaurant.getConversionDistance()));
        textView_restaurant_address.setText(restaurant.getAddress());


        if(!restaurant.getImage_base64().isEmpty()) {
            imageView_restaurant.setImageBitmap(Bitmap.createScaledBitmap(getBitmapFromString(restaurant.getImage_base64()), image_width, image_height, true));
        }

    }

    private void setUpListener() {
        toolbar_start_image.setOnClickListener(this);
        cardView_restaurant.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == toolbar_start_image){
            ApplicationController.finish(this);
        }
        else if (v == cardView_restaurant){
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }




    private void initMarkers() {
        MarkerOptions markerOptions;

        LatLng latLng = new LatLng(restaurant.getMapx(), restaurant.getMapy());
        markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_noclick)));

        marker = googleMap.addMarker(markerOptions);
        marker.setTag(restaurant);


        selected_index = 0;


        marker.setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_click)));
        googleMap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(37.56, 126.97)));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

//        googleMap.setOnMarkerClickListener(new MapMarkerClickListener(this));

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


            Location restaurantLocation = new Location(restaurant.getName());
            restaurantLocation.setLatitude(restaurant.getMapy());
            restaurantLocation.setLongitude(restaurant.getMapx());
            restaurant.setDistance(myLocation.distanceTo(restaurantLocation));

        } else {
            Log.d("aaaaaaaaaaa", "Current location is null. Using defaults.");

//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.56, 126.97), 10));
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

    private Bitmap getBitmapFromString(String jsonString) {
/*
* This Function converts the String back to Bitmap
* */
        byte[] decodedString = Base64.decode(jsonString, Base64.DEFAULT);

        return BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
    }

}
