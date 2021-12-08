package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static boolean online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private boolean logged() {
        File f = new File(getFilesDir()+"user.txt");
        return f.exists();
    }


    public boolean internetIsConnected() {
        return true;
    }

    public void playOnline(View view) {
        if(!internetIsConnected()){
            Toast.makeText(getBaseContext(), "Errore! Controllare la connessione", Toast.LENGTH_SHORT).show();
            return;
        }
        online = true;
        if(logged()){
            Intent i = new Intent(MainActivity.this, MenuActivity.class);
            startActivity(i);
        }else{
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
        finish();
    }

    public void playOffline(View view) {
        online = false;
        Intent i = new Intent(MainActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishAndRemoveTask();
    }
}