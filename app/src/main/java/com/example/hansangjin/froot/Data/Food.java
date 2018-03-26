package com.example.hansangjin.froot.Data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class Food implements Serializable, Comparable<Food> {
    private static final long serialVersionUID = -3435254357911427723L;

    private String name;
    private int price;
    private boolean possible;

    public Food() {
    }

    public Food(String name, int price, boolean possible) {
        this.name = name;
        this.price = price;
        this.possible = possible;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public boolean isPossible() {
        return possible;
    }

    public void setPossible(boolean possible) {
        this.possible = possible;
    }

    @Override
    public int compareTo(@NonNull Food o) {
        Boolean thisP = possible;

        return thisP.compareTo(o.isPossible());
    }
}
