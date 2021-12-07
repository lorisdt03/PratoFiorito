package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.content.res.AppCompatResources;
import java.io.File;

public class MenuActivity extends MyActivity {

    //starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_menu);



        ImageButton b = findViewById(R.id.audio_main);
        loadAudio(b);

        ring = newRing(this,R.raw.main_menu);
        if(!MainActivity.online){
            Button logOut = findViewById(R.id.logOut);
            logOut.setVisibility(View.INVISIBLE);
        }
    }

    //quando viene premuto un bottone delle difficolta
    // imposto la difficolta di conseguenza e inizio l'attività del gioco
    public void onClick(View view) {
        int id = view.getId();
        Button b = findViewById(id);
        b.setBackground(AppCompatResources.getDrawable(this,R.drawable.button_scaled_pressed));
        int difficulty;
        if(id==R.id.b_easy){
            difficulty = 0;
        }else if(id==R.id.b_normal){
            difficulty = 1;
        }else {
            difficulty = 2;
        }
        ring.pause();
        Intent i = new Intent(MenuActivity.this, GameActivity.class);
        i.putExtra("diff", difficulty);
        startActivity(i);
        GameActivity.flag=false;

    }
    //se viene premuto il bottone classifica apro la classifica
    public void ranks(View view) {
        int id = view.getId();
        Button b = findViewById(id);
        b.setBackground(AppCompatResources.getDrawable(this,R.drawable.button_scaled_pressed));
        Intent i = new Intent(MenuActivity.this, RankActivity.class);
        startActivity(i);
    }
    //se premo indietro termino il programma
    public void onBackPressed() {
        Intent i = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }

    public void logOut(View view) {
        File f = new File(getFilesDir()+"user.txt");
        if(f.delete()){
            Intent i = new Intent(MenuActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}