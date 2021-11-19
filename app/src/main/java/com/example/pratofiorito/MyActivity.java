package com.example.pratofiorito;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class MyActivity extends AppCompatActivity {
    public MediaPlayer ring;
    private boolean bool;
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
        if(ring!=null && bool){
            ring.start();
        }
    }
    //imposto che quando il gioco ricominci la musica riprenda
    @Override
    protected void onRestart() {
        super.onRestart();
        if(ring!=null && bool){
            ring.start();
        }
    }
    //creo un nuovo lettore audio con come musica riprodotta id
    public MediaPlayer newRing(Context cont, int id){
        ring= MediaPlayer.create(cont, id);
        ring.setLooping(true);
        ring.start();
        if(!bool){
            ring.pause();
        }
        return ring;
    }
    public void invertiAudio(View v){
        Log.d("Audio","invertendo");
        int id = v.getId();
        ImageButton b = findViewById(id);
        setAudio(!leggiAudio(),b);
    }
    public void gestisciAudio(ImageButton b){
        setAudio(leggiAudio(),b);
    }
    public void setAudio(boolean isActive, ImageButton b){
        Log.d("Audio","audio = "+isActive);
        if(isActive){
            b.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.audio));
            b.setSoundEffectsEnabled(true);
            if(ring!=null && !ring.isPlaying()){
                ring.start();
            }
        }else{
            b.setImageDrawable(AppCompatResources.getDrawable(this,R.drawable.audio_muted));
            b.setSoundEffectsEnabled(false);
            if(ring!=null && ring.isPlaying()){
                ring.pause();
            }
        }
        bool = isActive;
        scriviAudio(isActive);
    }
    public boolean leggiAudio(){
        String filePath = getFilesDir()+"audio.txt";
        File my_file;
        try {
            my_file = new File(filePath);
            if(!my_file.exists()){
                Log.d("Audio","Il file era inesistente");
                if(!my_file.createNewFile()){
                    finish();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            BufferedReader r = new BufferedReader(new FileReader(getFilesDir()+"audio.txt"));
            String s = r.readLine();
            r.close();
            Log.d("Audio","1 leggendo = "+s);
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Audio","leggendo errore ");
        return true;
    }
    private void scriviAudio(boolean isActive) {
        String filePath = getFilesDir()+"audio.txt";
        Log.d("Audio","scrivendo = "+isActive);
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try{
                writer.write(Boolean.toString(isActive));
            }catch (Exception e){
                e.printStackTrace();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
