package com.example.pratofiorito;

public class User {

    private final String name;
    private final String password;

    //creo un nuovo utente con il nome e la password passati
    public User(String n, String p) {
        name = n.toUpperCase();
        password = p;
    }

    //restituisco il nome dell'utente
    public String getName() {
        return name;
    }

    //restituisco la password dell'utente
    public String getPassword() {
        return password;
    }

}
