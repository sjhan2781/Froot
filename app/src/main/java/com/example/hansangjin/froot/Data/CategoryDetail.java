package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by sjhan on 2018-04-22.
 */

public class CategoryDetail implements Serializable{
    private static final long serialVersionUID = 8410753030890516415L;

    private int id;
    private String category_datail;

    public CategoryDetail() {
    }

    public CategoryDetail(int id, String category_datail) {
        this.id = id;
        this.category_datail = category_datail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory_datail() {
        return category_datail;
    }

    public void setCategory_datail(String category_datail) {
        this.category_datail = category_datail;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
