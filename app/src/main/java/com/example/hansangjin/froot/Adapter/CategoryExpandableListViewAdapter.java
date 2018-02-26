package com.example.hansangjin.froot.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.hansangjin.froot.Activities.CategoryActivity;
import com.example.hansangjin.froot.ApplicationController;
import com.example.hansangjin.froot.Data.Category;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 2. 22..
 */

public class CategoryExpandableListViewAdapter extends BaseExpandableListAdapter implements
        CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener, ExpandableListView.OnGroupExpandListener {
    private ArrayList<Category> categories = null;
    private CategoryActivity activity = null;
    private LayoutInflater inflater = null;
    private ArrayList<RadioGroup> categoryGroups = null;
    private ArrayList<CheckBox> allergyCheckBoxes = null;

    private enum CATEGORY_TYPE {TYPE_RELIGION, TYPE_VEGETARIAN, TYPE_ALLERGY}

    private static final int TYPE_RELIGION = 0;
    private static final int TYPE_VEGETARIAN = 1;
    private static final int TYPE_ALLERGY = 2;
    private static int SELECTED_TYPE = -1;

    private ExpandableListView expandableListView;

    public CategoryExpandableListViewAdapter(CategoryActivity activity, ExpandableListView parent, ArrayList<Category> categories) {
        this.activity = activity;
        this.categories = categories;
        this.expandableListView = parent;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.categoryGroups = new ArrayList<>();
        this.allergyCheckBoxes = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return categories.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return categories.get(groupPosition).getCategory();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getLists().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        int backgroundColor;
        int textColor;
        Bitmap indicatorBitmap;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.itemview_parent_list, parent, false);
        }

        if (isExpanded) {
            backgroundColor = activity.getResources().getColor(R.color.logoColor);
            textColor = activity.getResources().getColor(R.color.textColor_category_selected);
            indicatorBitmap = ApplicationController.setUpImage(R.drawable.ic_keyboard_arrow);
        } else {
            backgroundColor = activity.getResources().getColor(R.color.white);
            textColor = activity.getResources().getColor(R.color.textColor_category_unselected);
            indicatorBitmap = ApplicationController.setUpImage(R.drawable.ic_keyboard_arrow_left);
        }

        TextView textView = convertView.findViewById(R.id.textView_category);
        textView.setText((String) getGroup(groupPosition));
        textView.setTextColor(textColor);

        ImageView indicator = convertView.findViewById(R.id.indicator);
        indicator.setImageBitmap(indicatorBitmap);

        convertView.setBackgroundColor(backgroundColor);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        RadioGroup radioGroup;
        int groupType = getGroupType(groupPosition);


        if (convertView == null || (int) convertView.getTag() != groupType) {
            convertView = inflater.inflate(R.layout.itemview_child_list, parent, false);

            radioGroup = convertView.findViewById(R.id.radioGroup);
            radioGroup.setTag(groupType);
            categoryGroups.add(radioGroup);

            switch (groupType) {
                case TYPE_RELIGION:
                case TYPE_VEGETARIAN:
                    convertView.setTag(groupType);

                    for (int i = 0; i < categories.get(groupPosition).getLists().size(); i++) {
                        RadioButton radioButton = new RadioButton(activity);
                        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        radioButton.setText((String) getChild(groupPosition, i));

//                        radioButton.setOnCheckedChangeListener();
                        radioGroup.setOnCheckedChangeListener(this);
                        radioGroup.addView(radioButton, i, layoutParams);
                    }
                    break;
                case TYPE_ALLERGY:
                    convertView.setTag(groupType);

                    for (int i = 0; i < categories.get(groupPosition).getLists().size(); i++) {
                        CheckBox checkBox = new CheckBox(activity);
                        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        checkBox.setText((String) getChild(groupPosition, i));

                        allergyCheckBoxes.add(checkBox);

//                    checkBox.setOnCheckedChangeListener();
                        radioGroup.addView(checkBox, i, layoutParams);
                    }
                    break;
                default:
                    break;
            }
        }

        for (RadioGroup tmpGroup : categoryGroups){
            if ((int)tmpGroup.getTag() != groupType){
                tmpGroup.clearCheck();
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    @Override
    public int getGroupType(int groupPosition) {
        return categories.get(groupPosition).getType();
    }

    @Override
    public int getGroupTypeCount() {
        return CATEGORY_TYPE.values().length;
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        boolean isEnable = false;
        if (group.getCheckedRadioButtonId() != -1) {
            isEnable = true;
        }
        activity.setRegisterEnable(isEnable);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

    }

    @Override
    public void onGroupExpand(int groupPosition) {
        for(int i = 0; i < categories.size(); i++){
            if(i != groupPosition){
                expandableListView.collapseGroup(i);
            }
        }
        SELECTED_TYPE = categories.get(groupPosition).getType();
    }

    public void getSelectedCategory(){
        ArrayList<String> selectedCategory = new ArrayList<>();

        switch (SELECTED_TYPE){
            case TYPE_RELIGION:
//                selectedCategory.add(categories)
            case TYPE_VEGETARIAN:
            case TYPE_ALLERGY:
                break;
        }
    }
}
