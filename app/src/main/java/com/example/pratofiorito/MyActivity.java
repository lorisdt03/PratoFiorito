package com.example.pratofiorito;

import android.content.Context;
import android.media.MediaPlayer;

import androidx.appcompat.app.AppCompatActivity;

public class MyActivity extends AppCompatActivity {
    public MediaPlayer ring;
    //imposto che quando il gioco va in pausa la musica si fermi
    @Override
    protected void onPause() {
        if(ring.isPlaying()) {
            ring.pause();
        }
        super.onPause();
    }
    //imposto che quando il gioco si ferma la musica si fermi
    @Override
    protected void onStop() {
        if(ring.isPlaying()) {
            ring.pause();
        }
        super.onStop();
    }
    //imposto che quando il gioco rimprenda la musica riprenda
    @Override
    protected void onResume() {
        super.onResume();
        if(ring!=null){
            ring.start();
        }
    }
    //imposto che quando il gioco ricominci la musica riprenda
    @Override
    protected void onRestart() {
        super.onRestart();
        if(ring!=null){
            ring.start();

        }
    }
    //creo un nuovo lettore audio con come musica riprodotta id
    public MediaPlayer newRing(Context cont, int id){
        ring= MediaPlayer.create(cont, id);
        ring.setLooping(true);
        ring.start();
        return ring;
    }

}
