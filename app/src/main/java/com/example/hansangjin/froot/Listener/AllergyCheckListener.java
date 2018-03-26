package com.example.hansangjin.froot.Listener;

import android.widget.CompoundButton;

import com.example.hansangjin.froot.Activities.RestaurantMapActivity;
import com.example.hansangjin.froot.Adapter.CategoryExpandableListViewAdapter;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 1. 23..
 */

public class AllergyCheckListener implements CompoundButton.OnCheckedChangeListener {
    private ArrayList<String> selectedItem;
    private CategoryExpandableListViewAdapter adapter;

    public AllergyCheckListener(CategoryExpandableListViewAdapter adapter) {
        selectedItem = new ArrayList<>();
        this.adapter = adapter;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isChecked){
            selectedItem.add(buttonView.getText().toString());
        }
        else{
            selectedItem.remove(buttonView.getText().toString());
        }
    }

    public ArrayList<String> getSelectedItem(){
        return selectedItem;
    }

    public boolean isSelected(){
        return selectedItem.isEmpty();
    }
}
