package com.example.hansangjin.froot.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 2. 22..
 */

public class CategoryMain implements Serializable {
    private static final long serialVersionUID = -8314592799756162670L;
    private int id;
    private String category;
    private int type;
    private ArrayList<CategoryDetail> details;


    public CategoryMain(int id, String category, int type, ArrayList<CategoryDetail> lists) {
        this.id = id;
        this.category = category;
        this.details = lists;
        this.type = type;
    }

    public CategoryMain(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public ArrayList<CategoryDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<CategoryDetail> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "CategoryMain{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", type=" + type +
                ", details=" + details +
                '}';
    }
}
