package com.example.pratofiorito;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class DAOUser {
    private final FirebaseFirestore db;
    private final Context con;

    //mi connetto al database
    public DAOUser(Context con) {
        this.con = con;
        db = FirebaseFirestore.getInstance();
    }

    //aggiungo al database l'oggetto di tipo User passato come attributo
    public void add(User u) {
        Map<String, Object> user = new HashMap<>();
        user.put("uname", u.getName());
        user.put("password", u.getPassword());
        db.collection("Users").document(u.getName()).set(user);
    }

    //ottengo dal database la password corrispondente all'utente con il nome passato come attributo
    public String getPassword(String name) {
        DocumentReference cr = db.collection("Users").document(name);
        Task<DocumentSnapshot> t = cr.get();
        Chronometer c = new Chronometer();
        c.start();
        while (!t.isComplete()) {
            if (c.elapsed() > 10000) {
                Toast.makeText(con, "Impossibile completare l'operazione, controllare la connessione", Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        Map<String, Object> m = Objects.requireNonNull(t.getResult()).getData();
        assert m != null;
        return (String) m.get("password");
    }

    //restituisco 1 se l'utente esiste, 0 se l'utente non esiste
    // e -1 se ci sono voluti più di 15 secondi per la richiesta (richiesta non valida)
    public int userExist(User u) {
        DocumentReference cr = db.collection("Users").document(u.getName());
        Task<DocumentSnapshot> t = cr.get();
        Chronometer c = new Chronometer();
        c.start();
        while (!t.isComplete()) {
            if (c.elapsed() > 15000) {
                return -1;
            }
        }
        return (t.isSuccessful() && Objects.requireNonNull(t.getResult()).exists()) ? 1 : 0;
    }
}
