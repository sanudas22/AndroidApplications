package com.example.a801135224_midterm;

import java.io.Serializable;

public class Data implements Serializable {
    String city, country;

    @Override
    public String toString() {
        return city+","+ country;
    }
}
