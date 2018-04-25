package com.example.hansangjin.froot.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hansangjin.froot.Adapter.RestaurantMapRecyclerViewAdapter;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Restaurant;
import com.example.hansangjin.froot.Listener.MapMarkerClickListener;
import com.example.hansangjin.froot.Listener.ScrollListener;
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

import java.util.ArrayList;
import java.util.Collections;

public class RestaurantMapActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, LocationListener {
    private final int MAP_ACTIVITY_CODE = 300;

    private final int LOCATION_PERMISSION_REQUEST_CODE = 0;

    private Intent intent;
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

    private ArrayList<ParcelableRestaurant> restaurantList;
    private ArrayList<Marker> markers;

    private String NAVER_CLIENT_ID;
    private String NAVER_CLIENT_SECRET;

    private LinearLayoutManager linearLayoutManager;
    private CameraPosition mCameraPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_map);

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

        restaurantList = intent.getParcelableArrayListExtra("restaurants");

        recyclerView = findViewById(R.id.restaurant_list_view);
        toolbar_start_image = findViewById(R.id.toolbar_button_left);
        textView_title = findViewById(R.id.toolbar_textView_title);
        toolbar_end_image = findViewById(R.id.toolbar_button_right);

        linearLayoutManager = new LinearLayoutManager(this);

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
        toolbar_start_image.setImageResource(R.drawable.ic_keyboard_arrow_left_black_48dp_w);
        toolbar_start_image.setOnClickListener(this);

        textView_title.setVisibility(View.VISIBLE);
        textView_title.setText(R.string.title_restaurant_map);

        textView_title.setTextColor(getResources().getColor(R.color.logoColor));
    }

    private void setUpData() {
        restaurantList = intent.getParcelableArrayListExtra("restaurants");

    }

    private void setUpUI() {

        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

//        recyclerView.setOnFlingListener(new RestaurantRecyclerViewFlingListener(this, recyclerView));
        recyclerView.addOnScrollListener(new ScrollListener(this, recyclerView.computeHorizontalScrollExtent()));

        recyclerView.setNestedScrollingEnabled(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setItemPrefetchEnabled(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(recyclerViewAdapter);

    }

    private void setUpListener() {
        recyclerViewAdapter.setItemClick(new RestaurantMapRecyclerViewAdapter.ItemClick() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(), RestaurantDetailActivity.class);
                intent.putExtra("restaurant", (Parcelable) restaurantList.get(position));
                startActivityForResult(intent, MAP_ACTIVITY_CODE);
                overridePendingTransition(R.anim.activity_right_in, R.anim.activity_not_move);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == toolbar_start_image){
            ApplicationController.finish(this);
        }
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }



    public void setSelectedChild(int position) {
        if (!markers.isEmpty()) {
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(markers.get(position).getPosition()));
            setMarkers(position);
        }
    }

    private void initMarkers() {
        Marker marker;
        MarkerOptions markerOptions;

        for (ParcelableRestaurant restaurant : restaurantList) {
            LatLng latLng = new LatLng(restaurant.getMapx(), restaurant.getMapy());
            markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_noclick)));

            marker = googleMap.addMarker(markerOptions);
            marker.setTag(restaurant);

            markers.add(marker);
        }

        selected_index = 0;

        if(!markers.isEmpty()) {
            markers.get(selected_index).setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_click)));
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(markers.get(0).getPosition()));
        }
    }

    public void setMarkers(int position) {
        if (position != selected_index) {
            Marker marker = markers.get(selected_index);

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_noclick)));
            marker.setZIndex(1.0f);

            selected_index = position;
            marker = markers.get(selected_index);

            marker.setIcon(BitmapDescriptorFactory.fromBitmap(ApplicationController.setUpImage(R.drawable.map_click)));
            marker.setZIndex(2.0f);
        }
    }

    public void animateCamera(int position) {
        if (position != selected_index) {
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

//            setMarkers(0);

            recyclerViewAdapter.notifyDataSetChanged();

        } else {
            Log.d("aaaaaaaaaaa", "Current location is null. Using defaults.");
            if (!restaurantList.isEmpty()) {
                setMarkers(0);
            }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(37.56, 126.97), 10));
            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
        }

        initMarkers();
        if (!restaurantList.isEmpty()) {
            setMarkers(0);
        }

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


}
