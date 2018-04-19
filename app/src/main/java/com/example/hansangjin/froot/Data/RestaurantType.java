package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by sjhan on 2018-04-19.
 */

public class RestaurantType implements Serializable {
    private static final long serialVersionUID = 3308564069264453433L;
    private int id;
    private String type;

    public RestaurantType() {
    }

    public RestaurantType(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
