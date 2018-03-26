package com.example.hansangjin.froot.CustomView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import com.example.hansangjin.froot.Activities.RestaurantListActivity;
import com.example.hansangjin.froot.Adapter.LocationGridViewAdapter;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

public class MyBottomSheetDialogFragment extends BottomSheetDialogFragment implements View.OnClickListener{
    private Button button;
    private GridView location_gridView;
    private ArrayList<String> locations;
    private LocationGridViewAdapter adapter;

    public static MyBottomSheetDialogFragment newInstance(ArrayList<String> locationList) {
        MyBottomSheetDialogFragment f = new MyBottomSheetDialogFragment();
        Bundle args = new Bundle();
//        args.putString("string", string);
        args.putStringArrayList("locations", locationList);
        f.setArguments(args);


        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locations = getArguments().getStringArrayList("locations");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.bottom_sheet_modal, container, false);

        button = v.findViewById(R.id.button);
        location_gridView = v.findViewById(R.id.gridView);

        locations = getArguments().getStringArrayList("locations");

        adapter = new LocationGridViewAdapter(locations, this);
        button.setOnClickListener(this);
        button.setEnabled(false);

        location_gridView.setAdapter(adapter);


        return v;
    }

    @Override
    public void onClick(View v) {
        if (v == button){
            ((RestaurantListActivity)getActivity()).setLocation(adapter.getSelectedPosition());
            this.dismiss();
        }
    }

    public void setButtonEnable(boolean enabled){
        button.setEnabled(enabled);
    }
}