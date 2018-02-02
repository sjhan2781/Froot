package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by hansangjin on 2018. 1. 18..
 */

public class Restaurant implements Serializable {
    private static final long serialVersionUID = -4977672600535828310L;

    private String name;
    private String link;
    private String category;
    private String description;
    private String telephone;
    private String address;
    private String mapx;
    private String mapy;

    public Restaurant() {
    }

    public Restaurant(String name) {
        this.name = name;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
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

    public String getMapx() {
        return mapx;
    }

    public void setMapx(String mapx) {
        this.mapx = mapx;
    }

    public String getMapy() {
        return mapy;
    }

    public void setMapy(String mapy) {
        this.mapy = mapy;
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
                ", mapx='" + mapx + '\'' +
                ", mapy='" + mapy + '\'' +
                '}';
    }
}
