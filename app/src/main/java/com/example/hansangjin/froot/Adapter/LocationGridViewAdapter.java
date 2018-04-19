package com.example.hansangjin.froot.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.hansangjin.froot.CustomView.MyBottomSheetDialogFragment;
import com.example.hansangjin.froot.ParcelableData.ParcelableLocation;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by sjhan on 2018-03-09.
 */

public class LocationGridViewAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    private ArrayList<ParcelableLocation> locations;
    private MyBottomSheetDialogFragment fragment;
    private ArrayList<RadioButton> radioButtons;
    private int selectedPos;

    public LocationGridViewAdapter(ArrayList<ParcelableLocation> locations, MyBottomSheetDialogFragment fragment) {
        this.locations = locations;
        this.fragment = fragment;
        radioButtons = new ArrayList<>();
    }

    @Override
    public int getCount() {
        return locations.size();
    }

    @Override
    public Object getItem(int position) {
        return locations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        RadioButton radioButton = new RadioButton(context);

        if(convertView == null){
            LayoutInflater inflater = fragment.getLayoutInflater();
            convertView = inflater.inflate(R.layout.layout_location_radio_button, null);
//            convertView = new RadioButton(fragment.getContext());

            RadioButton radioButton = convertView.findViewById(R.id.radioButton_location);
            radioButton.setId(position);
            radioButton.setText(locations.get(position).getLocation());
            radioButton.setOnCheckedChangeListener(this);

            radioButtons.add(radioButton);
        }

        return convertView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            selectedPos = buttonView.getId();
            for (int i = 0; i < radioButtons.size(); i++) {
                if (!radioButtons.get(i).equals(buttonView)){
                    radioButtons.get(i).setChecked(false);
                }
            }
            fragment.setButtonEnable(true);
        }
    }

    public int getSelectedPosition(){
        return selectedPos;
    }
}
