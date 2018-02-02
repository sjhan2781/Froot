package com.example.hansangjin.froot.Fragment;

/**
 * Created by hansangjin on 2018. 1. 19..
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hansangjin.froot.Naver.NMapPOIflagType;
import com.example.hansangjin.froot.Naver.NaverMapPoint;
import com.example.hansangjin.froot.R;
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

/**
 * NMapFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용한 예제임.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
public class NMapFragment extends Fragment implements NMapView.OnMapStateChangeListener {
    private NMapView mapView;
    private NMapContext mMapContext;
    private NMapController mapController;

    //맵 마커 관련
    private NaverMapPoint nMapResourceProvider;
    private NMapOverlayManager nMapOverlayManager;

    //내 위치 관련
    private NMapLocationManager locationManager;

    private String NAVER_CLIENT_ID;

    /**
     * Fragment에 포함된 NMapView 객체를 반환함
     */
    private NMapView findMapView(View v) {

        if (!(v instanceof ViewGroup)) {
            return null;
        }

        ViewGroup vg = (ViewGroup) v;
        if (vg instanceof NMapView) {
            return (NMapView) vg;
        }

        for (int i = 0; i < vg.getChildCount(); i++) {

            View child = vg.getChildAt(i);
            if (!(child instanceof ViewGroup)) {
                continue;
            }

            NMapView mapView = findMapView(child);
            if (mapView != null) {
                return mapView;
            }
        }
        return null;
    }

	/* Fragment 라이프사이클에 따라서 NMapContext의 해당 API를 호출함 */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMapContext = new NMapContext(super.getActivity());

        NAVER_CLIENT_ID = getResources().getString(R.string.naver_api_client_key);

        mMapContext.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

//        mapView = new NMapView(getActivity());
//        mapView.setClientId(NAVER_CLIENT_ID);
//        mapView.setClickable(true);
//        mapView.setEnabled(true);
//        mapView.setFocusable(true);
//        mapView.setFocusableInTouchMode(true);
//        mapView.requestFocus();

//        return mapView;

        throw new IllegalArgumentException("onCreateView should be implemented in the subclass of NMapFragment.");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Fragment에 포함된 NMapView 객체 찾기
        NMapView mapView = findMapView(super.getView());
        if (mapView == null) {
            throw new IllegalArgumentException("NMapFragment dose not have an instance of NMapView.");
        }

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.

        mMapContext.setupMapView(mapView);

        // initialize map view
        mapView.setClientId(NAVER_CLIENT_ID);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        mapView.setOnMapStateChangeListener(this);
        mapView.setBuiltInZoomControls(true, null);
        mapView.setScalingFactor(2f, false);

        nMapResourceProvider = new NaverMapPoint(getActivity());

        nMapOverlayManager = new NMapOverlayManager(getActivity(), mapView, nMapResourceProvider);
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) {    //에러 없을때
            mapController = nMapView.getMapController();

            mapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);

            int markerId = NMapPOIflagType.PIN;

            // set POI data
            NMapPOIdata poiData = new NMapPOIdata(2, nMapResourceProvider);

            poiData.beginPOIdata(2);
            poiData.addPOIitem(127.0630205, 37.5091300, "Pizza 777-111", markerId, 0);
            poiData.addPOIitem(127.061, 37.51, "Pizza 123-456", markerId, 0);
            poiData.endPOIdata();

            // create POI data overlay
            NMapPOIdataOverlay poiDataOverlay = nMapOverlayManager.createPOIdataOverlay(poiData, null);

            poiDataOverlay.showAllPOIdata(0);
        } else {
            Log.e("Error", "onMapInitHandler: error = " + nMapError.toString());
        }
    }

    @Override
    public void onMapCenterChange(NMapView nMapView, NGeoPoint nGeoPoint) {

    }

    @Override
    public void onMapCenterChangeFine(NMapView nMapView) {

    }

    @Override
    public void onZoomLevelChange(NMapView nMapView, int i) {

    }

    @Override
    public void onAnimationStateChange(NMapView nMapView, int i, int i1) {

    }

    @Override
    public void onStart() {
        mMapContext.onStart();

////        mapView.setOnMapStateChangeListener(this);
//        mapView.setBuiltInZoomControls(true, null);
//        mapView.setScalingFactor(2f, false);
//
//        mapController = mapView.getMapController();
//
//        nMapResourceProvider = new NaverMapPoint(getActivity());
//
//        nMapOverlayManager = new NMapOverlayManager(getActivity(), mapView, nMapResourceProvider);

        super.onStart();

    }


    @Override
    public void onResume() {
        super.onResume();

        mMapContext.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        mMapContext.onPause();
    }

    @Override
    public void onStop() {

        mMapContext.onStop();

        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        mMapContext.onDestroy();

        super.onDestroy();
    }
}