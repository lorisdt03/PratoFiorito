package com.example.pratofiorito;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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

        ImageButton b = findViewById(R.id.audio_win);
        loadAudio(b);

        ring = newRing(this, R.raw.win);
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

        if (MainActivity.online) {
            TextView nome = findViewById(R.id.txt_nome);
            nome.setText(onlineName());
            name.setVisibility(View.INVISIBLE);
        } else {
            name.setOnEditorActionListener(new EnterListener(this));
        }

        filePath = getFilesDir() + "myfile.txt";
    }

    //leggo dal file "user.txt" il nome utente che è stato salvato al login
    private String onlineName() {
        String filePath = getFilesDir() + "user.txt";
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
            BufferedReader r = new BufferedReader(new FileReader(getFilesDir() + "user.txt"));
            String s = r.readLine();
            r.close();
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "ERRORE";
    }

    //alla pressione del bottone "invio" salvo i dati e torno al menu principale
    private void saveRankOnline() {
        DAOMyData dao = new DAOMyData();
        dao.add(gameData);
    }

    public void onClick(View view) {
        if (MainActivity.online) {
            gameData.setName(onlineName());
            saveRankOnline();
        } else {
            String n = getName(name.getText().toString());
            gameData.setName(n);
            saveRank();
        }
        goBack();
    }

    //restituisco il nome di lunghezza massimo 12 e senza spazi in fondo
    private String getName(String n) {
        if(n.length()==0)
            return "----------";
        if (n.charAt(0) == ' ') {
            if (n.length() == 1) {
                return "";
            }
            return getName(n.substring(1, n.length() - 1));
        }
        if (n.charAt(n.length() - 1) == ' ') {
            return getName(n.substring(0, n.length() - 1));
        }
        final String nSubstring = n.substring(0, Math.min(n.length(), 12));
        if (n.equals(nSubstring))
            return n;
        return getName(nSubstring);
    }

    //torno al menu principale
    private void goBack() {
        ring.stop();
        Intent i = new Intent(WinActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }

    //salvo i dati
    private void saveRank() {
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

        ArrayList<MyData> a = new ArrayList<>();
        loadRanks(a);
        a.add(gameData);
        a = sortData(a);
        if (a.size() > RankActivity.DIM_LIST)
            a.remove(a.size() - 1);
        writeRanks(a);
    }

    //carico i dati precedentemente salvati sul file myfile.txt
    private void loadRanks(ArrayList<MyData> a) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(getFilesDir() + "myfile.txt"));
            String line;
            while ((line = br.readLine()) != null) {
                a.add(new MyData(line));
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //riordino l'arraylist contenente la classifica
    private ArrayList<MyData> sortData(ArrayList<MyData> a) {
        int n_elem = Math.min(21, a.size());
        long[] scores = new long[n_elem];
        for (int i = 0; i < n_elem; i++) {
            scores[i] = a.get(i).getScore();
        }
        Arrays.sort(scores);
        int attuale = 0;
        int searched = 0;
        ArrayList<MyData> a1 = new ArrayList<>();
        while (a.size() != 0) {
            if (a.get(attuale).getScore() == scores[searched]) {
                a1.add(a.get(attuale));
                a.remove(attuale);
                searched++;
                attuale = 0;
            } else {
                attuale++;
            }

        }
        Collections.reverse(a1);
        return a1;
    }

    //salvo la classifica sul file myfile.txt
    private void writeRanks(ArrayList<MyData> a) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            try {
                for (int i = 0; i < RankActivity.DIM_LIST; i++) {
                    writer.write(a.get(i).toString() + System.getProperty("line.separator"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //quando premo il tasto indietro richiamo il metodo goback
    public void onBackPressed() {
        goBack();
    }
}