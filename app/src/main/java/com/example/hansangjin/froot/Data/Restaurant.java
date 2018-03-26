package com.example.hansangjin.froot.Data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 1. 18..
 */

public class Restaurant implements Serializable, Comparable<Restaurant> {
    private static final long serialVersionUID = -4977672600535828310L;

    private int ID;
    private String name;
    private String link;
    private int category;
    private String description;
    private String telephone;
    private String address;
    private double distance;
    private double mapx;
    private double mapy;
    private ArrayList<Food> foods;

    public Restaurant() {
        foods = new ArrayList<>();
    }

    public Restaurant(int ID, String name, int category) {
        this.ID = ID;
        this.name = name;
        this.category = category;
        foods = new ArrayList<>();
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public ArrayList<Food> getFoods() {
        return foods;
    }

    public void setFoods(ArrayList<Food> foods) {
        this.foods = foods;
    }

    public String getConversionDistance(){
        if(getDistance() != 0) {
            DecimalFormat format = new DecimalFormat(".#");

            return format.format(getDistance() / 1000) + " km";
        }
        else{
            return "";
        }
    }

    @Override
    public int compareTo(@NonNull Restaurant o) {
        if(this.distance > o.getDistance()){
            return 1;
        } else if(this.distance == o.getDistance()){
            return 0;
        } else{
           return -1;
        }
    }

    @Override
    public String toString() {
        return "Restaurant{" +
                "name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                '}';
    }
}
