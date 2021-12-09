package com.example.pratofiorito;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TableLayout;

import androidx.appcompat.content.res.AppCompatResources;

public class GameActivity extends MyActivity {

    private boolean firstMove;
    public static boolean flag = false;
    private Field c;

    //in base alla difficoltà che viene passata dalla MainActivity creo un campo di difficoltà diversa e starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campo);
        TableLayout layout = findViewById(R.id.Table);
        firstMove = true;

        ImageButton b = findViewById(R.id.audio_game);
        loadAudio(b);

        ring = newRing(this, R.raw.in_game);
        Bundle extras = getIntent().getExtras();
        switch (extras.getInt("diff")) {
            case 0:
                c = new Field(layout, this, Field.EASY);
                break;
            case 1:
                c = new Field(layout, this, Field.NORMAL);
                break;
            case 2:
                c = new Field(layout, this, Field.HARD);
                break;
        }

    }

    //quando il pulsante della bandierina viene premuto cambio il suo aspetto
    //e faccio in modo che la prossima interazione sia quella desiderata (piazzare o levare la bandierina)
    public void turnFlag(View view) {
        if (c.isGameEnded())
            return;
        flag = !flag;
        ImageButton flagB = findViewById(R.id.flagButton);
        if (flag) {
            flagB.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.minesweeper_flag_on));
        } else {
            flagB.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.minesweeper_flag_off));
        }
    }

    //fermo la musica e torno al menu principale
    public void reset(View view) {
        ring.stop();
        Intent i = new Intent(GameActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    //fermo la musica e vado alla schermata di vittoria
    public void win(MyData d) {
        ring.stop();
        Intent i = new Intent(GameActivity.this, WinActivity.class);
        i.putExtra("time", d.getTime());
        i.putExtra("difficulty", d.getDifficulty());
        startActivity(i);
        finish();
    }

    //restituisco se sia la prima mossa
    public boolean isFirstMove() {
        return firstMove;
    }

    //imposto che non sia più la prima mossa
    public void setNotFirstMove() {
        firstMove = false;
    }

    //quando viene premuto il tasto indietro richiamo reset
    public void onBackPressed() {
        reset(findViewById(R.id.reset));
    }
}