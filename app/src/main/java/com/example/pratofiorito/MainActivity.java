package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends MyActivity {

    //starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_main);

        ring = newRing(this,R.raw.main_menu);

    }
    //quando viene premuto un bottone delle difficolta
    // imposto la difficolta di conseguenza e inizio l'attività del gioco
    public void onClick(View view) {
        int difficulty;
        if(view.getId()==R.id.b_easy){
            difficulty = 0;
        }else if(view.getId()==R.id.b_normal){
            difficulty = 1;
        }else {
            difficulty = 2;
        }
        ring.pause();
        Intent i = new Intent(MainActivity.this, GameActivity.class);
        i.putExtra("diff", difficulty);
        startActivity(i);
        GameActivity.flag=false;

    }
    //se viene premuto il bottone classifica apro la classifica
    public void ranks(View view) {
        Intent i = new Intent(MainActivity.this, RankActivity.class);
        startActivity(i);
    }
    //se premo indietro termino il programma
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}