package com.example.pratofiorito;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.installations.remote.TokenResult;

import java.util.HashMap;
import java.util.Map;

public class DAOUser {
    private final FirebaseFirestore db;
    private int id=0;

    public DAOUser(){
        db = FirebaseFirestore.getInstance();
    }

    public void add(User u){
        Map<String,Object> user = new HashMap<>();
        user.put("uname", u.getName());
        user.put("password", u.getPassword());
        db.collection("Users").document(u.getName()).set(user);
    }

    public String getPassword(String name){
        DocumentReference cr = db.collection("Users").document(name);
        Task<DocumentSnapshot> t =  cr.get();
        while(!t.isComplete()){}
        Map m = t.getResult().getData();
        return (String)m.get("password");
    }
    public boolean userExist(User u) {
        DocumentReference cr = db.collection("Users").document(u.getName());
        Task<DocumentSnapshot> t =  cr.get();
        while(!t.isComplete()){}
        return t.isSuccessful() && t.getResult().exists();
    }

}
