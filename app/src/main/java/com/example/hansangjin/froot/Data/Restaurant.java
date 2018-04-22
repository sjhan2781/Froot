package com.example.hansangjin.froot.Data;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.DecimalFormat;

/**
 * Created by hansangjin on 2018. 1. 18..
 */

public class Restaurant implements Serializable, Comparable<Restaurant> {
    private static final long serialVersionUID = -4977672600535828310L;

    private int ID;
    private String name;
    private int category;
    private String telephone;
    private String address;
    private double distance;
    private double mapx;
    private double mapy;
    private String image_base64;
    private int halal;
    private String foods;

    public Restaurant() {

    }

    public Restaurant(int ID, String name, int category) {
        this.ID = ID;
        this.name = name;
        this.category = category;

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

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
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

    public String getImage_base64() {
        return image_base64;
    }

    public void setImage_base64(String image_base64) {
        this.image_base64 = image_base64;
    }

    public int getHalal() {
        return halal;
    }

    public void setHalal(int halal) {
        this.halal = halal;
    }

    public String getFoods() {
        return foods;
    }

    public void setFoods(String foods) {
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
                "ID=" + ID +
                ", name='" + name + '\'' +
                ", category=" + category +
                ", telephone='" + telephone + '\'' +
                ", address='" + address + '\'' +
                ", distance=" + distance +
                ", mapx=" + mapx +
                ", mapy=" + mapy +
                ", image_base64='" + image_base64 + '\'' +
                ", halal=" + halal +
                ", foods='" + foods + '\'' +
                '}';
    }
}
