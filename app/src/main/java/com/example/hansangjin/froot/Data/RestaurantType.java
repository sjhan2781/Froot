package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by sjhan on 2018-04-19.
 */

public class RestaurantType implements Serializable {
    private static final long serialVersionUID = 3308564069264453433L;
    private int id;
    private String type;
    protected boolean enabled;

    public RestaurantType() {
    }

    public RestaurantType(int id, String type) {
        this.id = id;
        this.type = type;
        enabled = false;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
