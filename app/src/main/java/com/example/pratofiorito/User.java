package com.example.pratofiorito;

import java.util.Locale;

public class User {
    private String name;
    private String password;
    public User(){}
    public User(String n, String p){
        name = n.toUpperCase();
        password = p.toUpperCase();
    }

    public void setName(String name) {
        this.name = name.toUpperCase();
    }

    public void setPassword(String password) {
        this.password = password.toUpperCase();
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
