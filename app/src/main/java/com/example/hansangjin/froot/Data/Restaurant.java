package com.example.hansangjin.froot.Data;

import java.io.Serializable;

/**
 * Created by hansangjin on 2018. 1. 18..
 */

public class Restaurant implements Serializable {
    private static final long serialVersionUID = -4977672600535828310L;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
