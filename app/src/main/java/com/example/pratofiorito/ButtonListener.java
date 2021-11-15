package com.example.pratofiorito;

import android.view.View;
import android.widget.ImageButton;

public class ButtonListener implements View.OnClickListener {

    private final Campo c;
    private final ImageButton[][] b;
    private final GameActivity ga;
    private final MyImages mi;
    private final Chronometer chr;

    ButtonListener(Campo c){
        this.c=c;
        ga = c.getContext();
        b = c.getB();
        mi = new MyImages(ga);
        chr = new Chronometer();
        //alla generazione del campo starto un cronometro che servirà a determinare la lunghezza della partita
        chr.start();
    }
    @Override
    public void onClick(View v) {
        //salvo l'id del bottone premuto
        int B_id = v.getId();
        //se è la prima mossa
        if(ga.isFirstMove() && !GameActivity.flag){
            ga.setNotFirstMove();
            int [] pos = c.getButtonCoordinates(B_id);
            //se ho premuto su una bomba la sposto
            if(c.isBomb(pos[0],pos[1])){
                c.moveBomb(pos[0],pos[1]);
            }
            //genero il campo
            c.bombsToField();
        }
        //se la partita non è ancora finita
        if(!c.isGameEnded()){
            //se il giocatore vuole rimuovere il tasto premuto
            if(!GameActivity.flag){
                replacePressed(B_id);
            }//se il giocatore vuole piazzare una bandierina
            else{
                c.placeFlag(B_id);
            }
            //se le condizioni di vittoria si sono verificate eseguo il metodo win
            if(winController() && !c.isGameEnded()){
                win();
            }
        }
    }
    //in caso di vittoria fermo il cronometro, genero i dato che potrebbero venir salvati e blocco i bottoni nel loro stato attuale
    private void win() {
        chr.stop();
        MyData gameData = new MyData((long) chr.getSeconds(), c.getDifficulty());
        c.endGame();
        c.setNotClickable();
        ga.win(gameData);
    }
    //in caso di sconfitta mostro tutto il campo e blocco i bottoni nel loro stato attuale
    private void lose() {
        c.endGame();
        c.setNotClickable();
        c.showField();
    }
    //controllo lo stato del campo per sapere se la partita è stata vinta o meno
    private boolean winController() {
        for(int i=0;i<Campo.DIM;i++){
            for(int j=0;j<Campo.DIM;j++){
                //Log.d("LoopVittoria","i "+i+" j "+j);
                if((mi.isButton(b[i][j]) || mi.isFlag(b[i][j])) && c.getMatBombe()[i][j]!=-1)
                    return false;
            }
        }
        return true;
    }
    //rimpiazzio il bottone che è stato premuto (con id = B_id) con l'immagine corrispondente
    public void replacePressed(int B_id) {
        ImageButton b = c.getButton(B_id);
        if(!mi.isFlag(b)){
            c.replaceElement(B_id);
            if(mi.isBomb(b)){
                lose();
            }else if(mi.isEmpty(b)){
                c.replaceNearEmpty(B_id);
            }
        }
    }
}
