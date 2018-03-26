package com.example.hansangjin.froot.Adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.hansangjin.froot.CustomView.RecyclerViewFragment;

import java.util.ArrayList;


public class CustomPagerAdapter extends FragmentPagerAdapter {
    ArrayList<RecyclerViewFragment> fragments;
    ArrayList<String> titles;

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
        fragments = new ArrayList<>();
        titles = new ArrayList<>();
    }

    @Override
    public RecyclerViewFragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles.get(position);
    }

    public void addFragment(RecyclerViewFragment fragment, String title){
        fragments.add(fragment);
        titles.add(title);
    }
}
