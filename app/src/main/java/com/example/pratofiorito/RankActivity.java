package com.example.pratofiorito;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.content.res.AppCompatResources;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class RankActivity extends MyActivity {

    private ArrayList<MyData> a;
    private LinearLayout ll;
    public static final int COLUMNS = 4;
    private MediaPlayer ring;
    public static int DIM_LIST = 20;
    private Drawable border;
    //salvo il layout che userò per stampare il campo, carico la classifica, la stampo e starto la musica
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);

        ImageButton b = findViewById(R.id.audio_rank);
        loadAudio(b);

        ring = newRing(this,R.raw.ranks);

        ll = findViewById(R.id.rank_layout);
        ll.setGravity(Gravity.CENTER_HORIZONTAL);

        border = AppCompatResources.getDrawable(this,R.drawable.border);
        a = new ArrayList<>();
        if(MainActivity.online){
            DAOMyData dao = new DAOMyData();
            dao.printRanks(this);
        }else{
            loadRanks();
            printRanks();
        }

    }


    //riordino l'arraylist contenente la classifica
    private ArrayList<MyData> sortData(ArrayList<MyData> a) {
        int n_elem = a.size();
        long [] scores= new long[n_elem];
        for(int i=0;i<n_elem;i++){
            scores[i] = a.get(i).getScore();
        }
        Arrays.sort(scores);
        int attuale = 0;
        int searched = 0;
        ArrayList<MyData> a1 = new ArrayList<>();
        while(a1.size()!=20){
            if(a.get(attuale).getScore()==scores[(n_elem-1)-searched]){
                a1.add(a.get(attuale));
                a.remove(attuale);
                searched++;
                attuale=0;
            }
            else{
                attuale++;
            }

        }
        return a1;
    }
    //carico la classifica dal file chiamato myfile
    private void loadRanks() {
        File f = new File(getFilesDir()+"myfile.txt");
        if(f.exists()){
            try {
                BufferedReader br = new BufferedReader(new FileReader(f.getPath()));
                String line;
                while((line = br.readLine()) != null){
                    a.add(new MyData(line));
                }
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //riempio gli spazzi vuoti con dei MyData default
        for(int i = a.size(); i< DIM_LIST; i++){
            a.add(new MyData());
        }
        a = sortData(a);
    }
    //stampo a schermo la classifica
    private void printRanks() {
        for(int i = 0; i< Math.min(DIM_LIST,a.size()); i++){
            ll.addView(addViews(a.get(i)));
        }
    }
    //restituisco una tablerow contenete una riga della classifica
    private TableRow addViews(MyData d) {
        TableRow l = new TableRow(this);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        l.setGravity(Gravity.CENTER);

        TextView [] views = new TextView[COLUMNS];
        for(int i = 0; i< COLUMNS; i++){
            views[i] = new TextView(this);
            views[i].setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            views[i].setBackground(border);
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
        ring.stop();
        Intent i = new Intent(RankActivity.this, MenuActivity.class);
        startActivity(i);
        finish();
    }
    //alla pressione del tasto indietro richiamo il metodo goBack
    @Override
    public void onBackPressed() {
        goBack(findViewById(R.id.mainMenu));
    }
    //quando viene ripresa la schermata svuoto la classifica e la ririempio
    @Override
    protected void onRestart() {
        super.onRestart();
        removeRanks();
        printRanks();
    }
    //svuoto la classifica
    private void removeRanks() {
        ll.removeViews(1,DIM_LIST);
    }
}