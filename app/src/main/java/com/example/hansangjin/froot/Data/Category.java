package com.example.hansangjin.froot.Data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by hansangjin on 2018. 2. 22..
 */

public class Category implements Serializable {
    private static final long serialVersionUID = -8314592799756162670L;
    private String category;
    private int type;
    private ArrayList<String> lists;


    public Category(String category, int type, ArrayList<String> lists) {
        this.category = category;
        this.lists = lists;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ArrayList<String> getLists() {
        return lists;
    }

    public void setLists(ArrayList<String> lists) {
        this.lists = lists;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
