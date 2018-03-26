package com.example.hansangjin.froot.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.example.hansangjin.froot.CustomView.MyBottomSheetDialogFragment;

import java.util.ArrayList;

/**
 * Created by sjhan on 2018-03-09.
 */

public class LocationGridViewAdapter extends BaseAdapter implements CompoundButton.OnCheckedChangeListener{
    private ArrayList<String> locations;
    private MyBottomSheetDialogFragment fragment;
    private ArrayList<RadioButton> radioButtons;
    private int selectedPos;

    public LocationGridViewAdapter(ArrayList<String> locations, MyBottomSheetDialogFragment fragment) {
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
            convertView = new RadioButton(fragment.getContext());
            convertView.setId(position);
            ((RadioButton) convertView).setText(locations.get(position));
            ((RadioButton) convertView).setOnCheckedChangeListener(this);
            radioButtons.add((RadioButton) convertView);
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
