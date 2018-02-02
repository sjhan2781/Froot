package com.example.hansangjin.froot.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.hansangjin.froot.R;

/**
 * Created by hansangjin on 2018. 2. 2..
 */

public class MapFragment extends NMapFragment{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_naver_map, container, false);
    }
}
