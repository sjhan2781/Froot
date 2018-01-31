package com.example.hansangjin.froot.Listener;

import android.widget.CompoundButton;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 1. 23..
 */

public class AllergyCheckListener implements CompoundButton.OnCheckedChangeListener {
    private ArrayList<String> selectedItem;

    public AllergyCheckListener() {
        selectedItem = new ArrayList<>();
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
}
