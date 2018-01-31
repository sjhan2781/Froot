package com.example.hansangjin.froot.Naver;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.R;
import com.nhn.android.maps.NMapOverlayItem;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapResourceProvider;

/**
 * Created by hansangjin on 2018. 1. 19..
 */

public class NaverMapPoint extends NMapResourceProvider {
    Context mContext;

    public NaverMapPoint(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected int findResourceIdForMarker(int i, boolean b) {
        return 0;
    }

    private Drawable getScaledDrawable(int resID) {
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), resID);

        double ratio = (double) (ApplicationController.metrics.density / 4);
        int width = (int) (bitmap.getWidth() * ratio);
        int height = (int) (bitmap.getHeight() * ratio);

        Drawable drawable = new BitmapDrawable(mContext.getResources(), Bitmap.createScaledBitmap(bitmap, width, height, true));

        if(drawable == null){
            Log.d("Drawable", "null");
        }else{
            Log.d("Drawable", "not null");
            return drawable;
        }

        return drawable;
    }

    @Override
    protected Drawable getDrawableForMarker(int i, boolean b, NMapOverlayItem nMapOverlayItem) {
        return getScaledDrawable(R.drawable.button_facebook_login_9);
//        return mContext.getResources().getDrawable(R.drawable.button_facebook_login_9);
    }

    @Override
    public Drawable getCalloutBackground(NMapOverlayItem nMapOverlayItem) {
        return getScaledDrawable(R.drawable.login_background_1_large);
//        return mContext.getResources().getDrawable(R.drawable.logo_3);

    }

    @Override
    public String getCalloutRightButtonText(NMapOverlayItem nMapOverlayItem) {
        return null;
    }

    @Override
    public Drawable[] getCalloutRightButton(NMapOverlayItem nMapOverlayItem) {
        return new Drawable[0];
    }

    @Override
    public Drawable[] getCalloutRightAccessory(NMapOverlayItem nMapOverlayItem) {
        return new Drawable[0];
    }

    @Override
    public int[] getCalloutTextColors(NMapOverlayItem nMapOverlayItem) {
        return new int[0];
    }

    @Override
    public Drawable[] getLocationDot() {
        return new Drawable[0];
    }

    @Override
    public Drawable getDirectionArrow() {
        return mContext.getResources().getDrawable(R.drawable.button_google_login_10);
    }

    @Override
    public int getParentLayoutIdForOverlappedListView() {
        return 0;
    }

    @Override
    public int getOverlappedListViewId() {
        return 0;
    }

    @Override
    public int getLayoutIdForOverlappedListView() {
        return 0;
    }

    @Override
    public void setOverlappedListViewLayout(ListView listView, int i, int i1, int i2) {

    }

    @Override
    public int getListItemLayoutIdForOverlappedListView() {
        return 0;
    }

    @Override
    public int getListItemTextViewId() {
        return 0;
    }

    @Override
    public int getListItemTailTextViewId() {
        return 0;
    }

    @Override
    public int getListItemImageViewId() {
        return 0;
    }

    @Override
    public int getListItemDividerId() {
        return 0;
    }

    @Override
    public void setOverlappedItemResource(NMapPOIitem nMapPOIitem, ImageView imageView) {

    }
}
