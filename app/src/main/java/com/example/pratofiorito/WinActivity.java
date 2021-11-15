package com.example.pratofiorito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class WinActivity extends MyActivity {

    private MyData gameData;
    private EditText name;
    private MediaPlayer ring;
    private String filePath;
    //stampo a schermo tutti i dati della partita appena finita e starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win);
        ring = newRing(this,R.raw.win);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            long time = extras.getLong("time");
            int difficulty = extras.getInt("difficulty");
            gameData = new MyData(time, difficulty);
        }
        TextView score = findViewById(R.id.punti);
        TextView time = findViewById(R.id.tempo);
        score.setText(gameData.getScoreStr());
        time.setText(gameData.getTimeStr());
        name = findViewById(R.id.nome);
        name.setOnEditorActionListener(new EnterListener(this));
        filePath = getFilesDir()+"myfile.txt";
    }
    //alla pressione del bottone "invio" salvo i dati e torno al menu principale
    public void onClick(View v){
        String n = name.getText().toString();
        if(n.equals("")){
            gameData.setName("----------");
        }
        else{
            gameData.setName(n.substring(0,Math.min(n.length(),10)));
        }
        saveRank();
        goBack();
    }
    //torno al menu principale
    private void goBack() {
        ring.stop();
        Intent i = new Intent(WinActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
    //salvo i dati
    private void saveRank()  {
        File my_file;
        try {
            my_file = new File(filePath);
            if(!my_file.exists()){
                if(!my_file.createNewFile()){
                    finish();
                }
            }
        }catch (Exception e){}

        ArrayList<MyData> a = new ArrayList();
        loadRanks(a);
        a.add(gameData);
        a = sortData(a);
        if(a.size()>RankActivity.DIM_LIST)
            a.remove(a.size()-1);
        writeRanks(a);
    }
    //carico i dati precedentemente salvati sul file myfile.txt
    private void loadRanks(ArrayList a) {
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
    }
    //riordino l'arraylist contenente la classifica
    private ArrayList sortData(ArrayList a) {
        int n_elem = Math.min(21,a.size());
        long [] scores= new long[n_elem];
        for(int i=0;i<n_elem;i++){
            scores[i] = ((MyData)a.get(i)).getScore();
        }
        Arrays.sort(scores);
        int attuale = 0;
        int searched = 0;
        ArrayList a1 = new ArrayList();
        while(a.size()!=0){
            if(((MyData)a.get(attuale)).getScore()==scores[searched]){
                a1.add(a.get(attuale));
                a.remove(attuale);
                searched++;
                attuale=0;
            }
            else{
                attuale++;
            }

        }
        Collections.reverse(a1);
        return a1;
    }
    //salvo la classifica sul file myfile.txt
    private void writeRanks(ArrayList a) {
        try{
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try{
                for(int i = 0; i<RankActivity.DIM_LIST; i++){
                    writer.write(a.get(i).toString()+System.getProperty("line.separator"));
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    //quando premo il tasto indietro richiamo il metodo goback
    public void onBackPressed() {
        goBack();
    }
}