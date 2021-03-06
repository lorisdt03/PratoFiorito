package com.example.pratofiorito;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.content.res.AppCompatResources;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class DAOMyData {
    private final FirebaseFirestore db;
    private ArrayList<MyData> a;
    private RankActivity ra;
    private Drawable border;
    private Context con;
    //mi connetto al database
    public DAOMyData() {
        db = FirebaseFirestore.getInstance();
    }

    //aggiungo al database l'oggetto di tipo MyData passato come attributo
    public void add(MyData d) {
        Map<String, Object> data = new HashMap<>();
        data.put("uname", d.getName());
        data.put("difficulty", d.getDifficulty());
        data.put("time", d.getTime());
        db.collection("Records").document().set(data);
    }

    //stampo a schermo la classifica
    public void printRanks(RankActivity ra) {
        this.ra = ra;
        border = AppCompatResources.getDrawable(ra, R.drawable.border);
        a = new ArrayList<>();
        FirebaseFirestore.getInstance()
                .collection("Records")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<DocumentSnapshot> myListOfDocuments = Objects.requireNonNull(task.getResult()).getDocuments();
                        for (int i = 0; i < myListOfDocuments.size(); i++) {
                            Map<String, Object> m = myListOfDocuments.get(i).getData();
                            assert m != null;
                            String name = (String) m.get("uname");
                            int difficulty = ((Long) Objects.requireNonNull(m.get("difficulty"))).intValue();
                            int time = ((Long) Objects.requireNonNull(m.get("time"))).intValue();
                            a.add(new MyData(name, difficulty, time));
                        }
                        a = sortData(a);
                        while (a.size() > RankActivity.DIM_LIST - 1) {
                            a.remove(RankActivity.DIM_LIST - 1);
                        }
                        while (a.size() < RankActivity.DIM_LIST - 1) {
                            a.add(new MyData());
                        }
                        for (int i = 0; i < Math.min(RankActivity.DIM_LIST, a.size()); i++) {
                            LinearLayout ll = ra.findViewById(R.id.rank_layout);
                            ll.addView(addViews(a.get(i)));
                        }
                    }else{
                        Toast.makeText(con, "Errore di connessione", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    void setContext(Context con){
        this.con = con;
    }

    //riordino l'arraylist contenente la classifica
    private ArrayList<MyData> sortData(ArrayList<MyData> a) {
        int n_elem = a.size();
        long[] scores = new long[n_elem];
        for (int i = 0; i < n_elem; i++) {
            scores[i] = a.get(i).getScore();
        }
        Arrays.sort(scores);
        int attuale = 0;
        int searched = 0;
        ArrayList<MyData> a1 = new ArrayList<>();
        while (a1.size() != RankActivity.DIM_LIST) {
            if (a.get(attuale).getScore() == scores[(n_elem - 1) - searched]) {
                a1.add(a.get(attuale));
                a.remove(attuale);
                searched++;
                attuale = 0;
            } else {
                attuale++;
            }

        }
        return a1;
    }

    //restituisco una tablerow contenete una riga della classifica
    private TableRow addViews(MyData d) {
        TableRow l = new TableRow(ra);
        l.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        l.setGravity(Gravity.CENTER);

        TextView[] views = new TextView[RankActivity.COLUMNS];
        for (int i = 0; i < RankActivity.COLUMNS; i++) {

            views[i] = new TextView(ra);
            views[i].setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            views[i].setBackground(border);
        }

        views[0].setText(d.getScoreStr());
        views[1].setText(d.getTimeStr());
        views[2].setText(d.getDifficultyStr());
        views[3].setText(d.getName().toUpperCase(Locale.ROOT));
        for (int i = 0; i < RankActivity.COLUMNS; i++) {
            views[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            l.addView(views[i], ra.findViewById(R.id.score).getLayoutParams());
        }
        return l;
    }
}
