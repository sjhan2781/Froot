package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by hansangjin on 2018. 1. 25..
 */

public class Food implements Serializable {
    private static final long serialVersionUID = -3435254357911427723L;

    private String name;
    private int price;

    public Food() {
    }

    public Food(String name, int price) {
        this.name = name;
        this.price = price;
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
}
