package com.example.hansangjin.froot.Data;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class Food implements Serializable, Comparable<Food> {
    private static final long serialVersionUID = -3435254357911427723L;

    private int id;
    private String name;
    private int price;
    private String image_base64;
    private boolean available;

    public Food() {
    }

    public Food(int id, String name, int price, String image_base64, boolean available) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image_base64 = image_base64;
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public int compareTo(@NonNull Food o) {
        Boolean thisP = o.isAvailable();

        return thisP.compareTo(this.isAvailable());
    }
}
