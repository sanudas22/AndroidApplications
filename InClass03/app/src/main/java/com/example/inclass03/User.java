package com.example.inclass03;

import java.io.Serializable;

public class User implements Serializable {
    String firstName;
    String lastName;
    String gender;

    public User(String firstName, String lastName, String gender) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }
}
