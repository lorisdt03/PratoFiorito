package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    public static boolean online;

    //all'avvio del programma creo la MainActivity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //restituisco se esiste già un file contenente delle credenziali
    private boolean logged() {
        File f = new File(getFilesDir() + "user.txt");
        return f.exists();
    }

    //se l'utente è già loggato avvio una partita online, altrimenti lo faccio autenticare
    public void playOnline(View view) {
        online = true;
        if (logged()) {
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        } else {
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        finish();
    }

    //avvio una partita offline
    public void playOffline(View view) {
        online = false;
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    //alla pressione del tasto indietro termino l'applicazione
    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}