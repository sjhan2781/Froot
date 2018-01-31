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
import com.nhn.android.maps.NMapContext;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapLocationManager;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * NaverMapFragment 클래스는 NMapActivity를 상속하지 않고 NMapView만 사용하고자 하는 경우에 NMapContext를 이용한 예제임.
 * NMapView 사용시 필요한 초기화 및 리스너 등록은 NMapActivity 사용시와 동일함.
 */
public class NaverMapFragment extends Fragment implements NMapView.OnMapStateChangeListener {

    private NMapView mapView;
    private NMapContext mMapContext;
    private NMapController mapController;

    //맵 마커 관련
    private NaverMapPoint nMapResourceProvider;
    private NMapOverlayManager nMapOverlayManager;

    //내 위치 관련
    private NMapLocationManager locationManager;

    private final String NAVER_CLIENT_ID = "cffusdBSZjbG3dKWypex";
    private final String NAVER_CLIENT_SECRET = "0yJ9t0Bmfp";

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

        mMapContext.onCreate();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mapView = new NMapView(getActivity());
        mapView.setClientId(NAVER_CLIENT_ID);
        mapView.setClickable(true);
        mapView.setEnabled(true);
        mapView.setFocusable(true);
        mapView.setFocusableInTouchMode(true);
        mapView.requestFocus();

        return mapView;

//        throw new IllegalArgumentException("onCreateView should be implemented in the subclass of NaverMapFragment.");

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Fragment에 포함된 NMapView 객체 찾기
        NMapView mapView = findMapView(super.getView());
        if (mapView == null) {
            throw new IllegalArgumentException("NaverMapFragment dose not have an instance of NMapView.");
        }

        // NMapActivity를 상속하지 않는 경우에는 NMapView 객체 생성후 반드시 setupMapView()를 호출해야함.
        mMapContext.setupMapView(mapView);
    }

    @Override
    public void onMapInitHandler(NMapView nMapView, NMapError nMapError) {
        if (nMapError == null) {    //에러 없을때
            mapController.setMapCenter(new NGeoPoint(126.978371, 37.5666091), 11);

            int markerId = NMapPOIflagType.PIN;

//            searchLocation("한식");

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

    //지역 검색
    public void searchLocation(String keyword) {
        try {

            String text = URLEncoder.encode(keyword, "UTF8");
            String urlStr = "https://openapi.naver.com/v1/search/local.xml?query=" + text;
            URL url = null;
            url = new URL(urlStr);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("X-Naver-Client-ID", NAVER_CLIENT_ID); //발급받은ID
            connection.setRequestProperty("X-Naver-Client-Secret", NAVER_CLIENT_SECRET);//발급받은PW
            connection.setRequestProperty("Content-Type", "application/xml");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();

            // 스트림을 통해 파싱을 한다.
            parser.setInput(connection.getInputStream(), null);

            parseResult(parser);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void parseResult(XmlPullParser parser) {
        try {
            String tag;

            int xValue = 0, yValue = 0;
            String name = "", address = "", telNum = "";

            parser.next();

            int eventType = parser.getEventType();

            Log.d("aaaaaer", eventType + "");


            while (eventType != XmlPullParser.END_DOCUMENT) {

                switch (eventType) {

                    case XmlPullParser.START_DOCUMENT:

                        break;

                    case XmlPullParser.START_TAG:

                        tag = parser.getName();    //테그 이름 얻어오기

                        Log.d("Naver", tag);

                        if (tag.equals("item")) ;// 첫번째 검색결과

                        else if (tag.equals("title")) {
                            parser.next();

                            if (parser.getText().contains("Naver Open API")) {

                                break;

                            } else {

                                name = parser.getText().replace("<b>", "").replace("</b>", "");

                            }

                        } else if (tag.equals("address")) {

                            parser.next();

                            address = parser.getText();

                        } else if (tag.equals("telephone")) {

                            parser.next();

                            telNum = parser.getText();

                        } else if (tag.equals("mapx")) { // getmapx value

                            parser.next();

                            xValue = Integer.parseInt(parser.getText());

                        } else if (tag.equals("mapy")) { // getmapy valye

                            parser.next();

                            yValue = Integer.parseInt(parser.getText());

                        }
                        break;
                    case XmlPullParser.TEXT:

                        break;

                    case XmlPullParser.END_TAG:
                        tag = parser.getName();    //테그 이름 얻어오기

                        if (tag.equals("item"))

                            break;

                }

                eventType = parser.next();

                Log.d("Location", name + ", " + telNum + ", " + xValue + ", " + yValue);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        mMapContext.onStart();

        mapView.setOnMapStateChangeListener(this);
        mapView.setBuiltInZoomControls(true, null);
        mapView.setScalingFactor(2f, false);

        mapController = mapView.getMapController();

        nMapResourceProvider = new NaverMapPoint(getActivity());

        nMapOverlayManager = new NMapOverlayManager(getActivity(), mapView, nMapResourceProvider);
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