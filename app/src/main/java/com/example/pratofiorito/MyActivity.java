package com.example.pratofiorito;

import android.content.Context;
import android.media.MediaPlayer;
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

    protected MediaPlayer ring;
    private boolean bool;

    //imposto che quando il gioco va in pausa la musica si fermi
    @Override
    protected void onPause() {
        if (ring.isPlaying()) {
            ring.pause();
        }
        super.onPause();
    }

    //imposto che quando il gioco si ferma la musica si fermi
    @Override
    protected void onStop() {
        if (ring.isPlaying()) {
            ring.pause();
        }
        super.onStop();
    }

    //imposto che quando il gioco rimprenda la musica riprenda
    @Override
    protected void onResume() {
        super.onResume();
        if (ring != null && bool) {
            ring.start();
        }
    }

    //imposto che quando il gioco ricominci la musica riprenda
    @Override
    protected void onRestart() {
        super.onRestart();
        if (ring != null && bool) {
            ring.start();
        }
    }

    //creo un nuovo lettore audio con come musica riprodotta id
    protected MediaPlayer newRing(Context context, int Music_id) {
        ring = MediaPlayer.create(context, Music_id);

        ring.setLooping(true);
        ring.start();
        if (!bool) {
            ring.pause();
        }
        return ring;
    }

    //alla pressione del tasto dell'audio cambio l'immagine e inverto l'audio
    //(se sta andando lo muto, altrimenti lo faccio partire)
    public void changeAudio(View view) {
        int id = view.getId();
        ImageButton b = findViewById(id);
        setAudio(!readAudio(), b);
    }

    //leggo da file se l'audio era stato precedentemente mutato o no e lo imposto di conseguenza
    protected void loadAudio(ImageButton b) {
        setAudio(readAudio(), b);
    }

    //imposto l'audio a isActive (avvio o fermo la musica)
    private void setAudio(boolean isActive, ImageButton b) {
        if (isActive) {
            b.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.audio));
            b.setSoundEffectsEnabled(true);
            if (ring != null && !ring.isPlaying()) {
                ring.start();
            }
        } else {
            b.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.audio_muted));
            b.setSoundEffectsEnabled(false);
            if (ring != null && ring.isPlaying()) {
                ring.pause();
            }
        }
        bool = isActive;
        writeAudio(isActive);
    }

    //leggo da file se l'audio era stato precedentemente mutato o no
    private boolean readAudio() {
        String filePath = getFilesDir() + "audio.txt";
        File my_file;
        try {
            my_file = new File(filePath);
            if (!my_file.exists()) {
                if (!my_file.createNewFile()) {
                    finish();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            BufferedReader r = new BufferedReader(new FileReader(getFilesDir() + "audio.txt"));
            String s = r.readLine();
            r.close();
            return Boolean.parseBoolean(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    //scrivo su file se l'audio ?? stato mutato o no
    private void writeAudio(boolean isActive) {
        String filePath = getFilesDir() + "audio.txt";
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try {
                writer.write(Boolean.toString(isActive));
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
