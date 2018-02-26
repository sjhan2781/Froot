package com.example.hansangjin.froot.Data;

/**
 * Created by hansangjin on 2018. 2. 22..
 */

public class CategoryItemView {
    private int type;
    private String name;

    public CategoryItemView(int type, String name) {
        this.type = type;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
