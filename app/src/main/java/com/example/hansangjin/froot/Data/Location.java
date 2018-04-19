package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by sjhan on 2018-04-19.
 */

public class Location implements Serializable {
    private static final long serialVersionUID = -4916088263009284389L;
    private int id;
    private  String location;

    public Location() {
    }

    public Location(int id, String location) {
        this.id = id;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
