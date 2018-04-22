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
import com.example.hansangjin.froot.Data.CategoryDetail;
import com.example.hansangjin.froot.Data.CategoryMain;
import com.example.hansangjin.froot.R;

import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 2. 22..
 */

public class CategoryExpandableListViewAdapter extends BaseExpandableListAdapter implements
        CompoundButton.OnCheckedChangeListener, ExpandableListView.OnGroupExpandListener, ExpandableListView.OnGroupCollapseListener {
    private ArrayList<CategoryMain> categories = null;
    private CategoryActivity activity = null;
    private LayoutInflater inflater = null;
    private ArrayList<RadioGroup> radioGroups = null;
    private ArrayList<CheckBox> allergyCheckBoxes = null;
    private ArrayList<CategoryDetail> selectedItem = null;

    final int SINGLE_CHOICE = 0;
    final int MULTIPLE_CHOICE = 1;

    private static int MAIN_SELECTED_INDEX;

    private ExpandableListView expandableListView;

    public CategoryExpandableListViewAdapter(CategoryActivity activity, ExpandableListView parent) {
        this.activity = activity;
        this.categories = new ArrayList<>();
        this.expandableListView = parent;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.radioGroups = new ArrayList<>();
        this.allergyCheckBoxes = new ArrayList<>();
        this.selectedItem = new ArrayList<>();
    }


    public CategoryExpandableListViewAdapter(CategoryActivity activity, ExpandableListView parent, ArrayList<CategoryMain> categories) {
        this.activity = activity;
        this.categories = categories;
        this.expandableListView = parent;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.radioGroups = new ArrayList<>();
        this.allergyCheckBoxes = new ArrayList<>();
        this.selectedItem = new ArrayList<>();
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
        return categories.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getDetails().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return categories.get(groupPosition).getDetails().get(childPosition).getId();
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
            backgroundColor = activity.getResources().getColor(R.color.colorAccent);
            textColor = activity.getResources().getColor(R.color.textColor_category_selected);
            indicatorBitmap = ApplicationController.setUpImage(R.drawable.ic_keyboard_arrow_left_black_1);

            for (RadioGroup radioGroup : radioGroups) {
                radioGroup.clearCheck();
            }
            for (CheckBox checkBox : allergyCheckBoxes) {
                checkBox.setChecked(false);
            }

        } else {
            backgroundColor = activity.getResources().getColor(R.color.white);
            textColor = activity.getResources().getColor(R.color.textColor_category_unselected);
            indicatorBitmap = ApplicationController.setUpImage(R.drawable.ic_keyboard_arrow_left_black_24dp);
        }


        TextView textView = convertView.findViewById(R.id.textView_category);
        ImageView indicator = convertView.findViewById(R.id.indicator);

        if (textView != null) {
            textView.setText( ((CategoryMain)getGroup(groupPosition)).getCategory() );
            textView.setTextColor(textColor);
        }

        if (indicator != null) {
            indicator.setImageBitmap(indicatorBitmap);
        }

        convertView.setBackgroundColor(backgroundColor);


        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        RadioGroup radioGroup;

        if (convertView == null || ((CategoryMain)convertView.getTag()).getId() != ((CategoryMain) getGroup(groupPosition)).getId()) {
            convertView = inflater.inflate(R.layout.itemview_child_list, parent, false);

            radioGroup = convertView.findViewById(R.id.radioGroup);
            radioGroup.setTag(((CategoryMain)getGroup(groupPosition)).getId());

            switch (categories.get(groupPosition).getType()) {
                case SINGLE_CHOICE:
                    convertView.setTag(getGroup(groupPosition));
                    for (int i = 0; i < categories.get(groupPosition).getDetails().size(); i++) {
                        RadioButton radioButton = new RadioButton(activity);
                        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                        radioButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        radioButton.setText(((CategoryDetail) getChild(groupPosition, i)).getCategory_datail());
                        radioButton.setId(i);
                        radioButton.setTag(categories.get(groupPosition).getDetails().get(i));

                        radioButton.setOnCheckedChangeListener(this);
                        radioGroup.addView(radioButton, layoutParams);
                    }
                    radioGroups.add(radioGroup);
                    break;
                case MULTIPLE_CHOICE:
                    convertView.setTag(getGroup(groupPosition));

                    for (int i = 0; i < categories.get(groupPosition).getDetails().size(); i++) {
                        CheckBox checkBox = new CheckBox(activity);
                        LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                                RadioGroup.LayoutParams.MATCH_PARENT,
                                RadioGroup.LayoutParams.WRAP_CONTENT);

                        checkBox.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
                        checkBox.setText( ((CategoryDetail) getChild(groupPosition, i)).getCategory_datail() );
                        checkBox.setId(i);
                        checkBox.setTag(categories.get(groupPosition).getDetails().get(i));


                        allergyCheckBoxes.add(checkBox);

                        checkBox.setOnCheckedChangeListener(this);
                        radioGroup.addView(checkBox, layoutParams);
                    }
                    break;
            }
        }

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

//    @Override
//    public int getGroupType(int groupPosition) {
//        return categories.get(groupPosition).getId();
//    }
//
//    @Override
//    public int getGroupTypeCount() {
//        return categories.size();
//    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        if (isChecked) {
            selectedItem.add((CategoryDetail) buttonView.getTag());
        } else {
            selectedItem.remove((CategoryDetail) buttonView.getTag());
        }
        setRegisterButtonEnable();
    }

    @Override
    public void onGroupExpand(int groupPosition) {
        for (int i = 0; i < categories.size(); i++) {
            if (i != groupPosition) {
                expandableListView.collapseGroup(i);
            }
        }

        if (categories.get(groupPosition).getType() == 2) {
            activity.showToast();
        }

        MAIN_SELECTED_INDEX = groupPosition;
    }

    @Override
    public void onGroupCollapse(int groupPosition) {
        if (categories.get(groupPosition).getType() == 2) {
            activity.dismissToast();
        }
    }

    public void setRegisterButtonEnable() {
        activity.setRegisterEnable(!selectedItem.isEmpty());
    }

    public CategoryMain getSelectedCategory() {
        CategoryMain sel = categories.get(MAIN_SELECTED_INDEX);
        return new CategoryMain(sel.getId(), sel.getCategory(),sel.getType(), selectedItem);
    }

    public ArrayList<CategoryMain> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoryMain> categories) {
        this.categories = categories;
    }
}
