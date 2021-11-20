package com.example.pratofiorito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Locale;

public class RankActivity extends MyActivity {

    private ArrayList a;
    private LinearLayout ll;
    private static final int COLUMNS = 4;
    private MediaPlayer ring;
    public static int DIM_LIST = 20;

        //salvo il layout che userò per stampare il campo, carico la classifica, la stampo e starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ImageButton b = findViewById(R.id.audio_rank);
        gestisciAudio(b);

        ring = newRing(this,R.raw.ranks);
        ll = findViewById(R.id.rank_layout);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);
        a = new ArrayList();
        loadRanks();
        printRanks();

    }
    //carico la classifica dal file chiamato myfile
    private void loadRanks() {
        try {
            BufferedReader r = new BufferedReader(new FileReader(getFilesDir()+"myfile.txt"));
            try {
                while(true){
                    a.add(new MyData(r.readLine()));
                }
            }catch (Exception e){
            }
            r.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = a.size(); i< DIM_LIST; i++){
            a.add(new MyData());
        }
    }
    //stampo a schermo la classifica
    private void printRanks() {
        for(int i = 0; i< DIM_LIST; i++){
            ll.addView(addViews((MyData) a.get(i)));
        }
    }
    //restituisco una tablerow contenete una riga della classifica
    private TableRow addViews(MyData d) {

        TableRow l = new TableRow(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        l.setGravity(Gravity.CENTER);

        TextView [] views = new TextView[COLUMNS];
        MyImages mi = new MyImages(this);
        for(int i = 0; i< COLUMNS; i++){

            views[i] = new TextView(this);
            views[i].setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            views[i].setBackground(mi.getBorder());
        }

        views[0].setText(d.getScoreStr());
        views[1].setText(d.getTimeStr());
        views[2].setText(d.getDifficultyStr());
        views[3].setText(d.getName().toUpperCase(Locale.ROOT));
        for(int i = 0; i< COLUMNS; i++){
            views[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            l.addView(views[i],findViewById(R.id.score).getLayoutParams());
        }
        return l;
    }
    //fermo la musica e torno al menu principale
    public void goBack(View view) {
        int id = view.getId();
        Button b = findViewById(id);
        b.setBackground(AppCompatResources.getDrawable(this,R.drawable.button_scaled_small_pressed));
        ring.stop();
        Intent i = new Intent(RankActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    //alla pressione del tasto indietro richiamo il metodo goBack
    @Override
    public void onBackPressed() {
        goBack(findViewById(R.id.mainMenu));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        removeRanks();
        printRanks();
    }

    private void removeRanks() {
        ll.removeViews(1,DIM_LIST);
    }
}