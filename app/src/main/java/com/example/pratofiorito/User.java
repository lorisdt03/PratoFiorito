package com.example.pratofiorito;

public class User {
    private final String name;
    private final String password;
    public User(String n, String p){
        name = n.toUpperCase();
        password = p;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

}
