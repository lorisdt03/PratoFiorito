package com.example.pratofiorito;

import android.view.View;

public class ButtonListener implements View.OnClickListener {

    private final Field f;
    private final GameActivity ga;
    private final Chronometer chr;

    ButtonListener(Field f){
        this.f = f;
        ga = f.getContext();
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
            int [] pos = f.getButtonCoordinates(B_id);
            //se ho premuto su una bomba la sposto
            if(f.isBomb(pos[0],pos[1])){
                f.moveBomb(pos[0],pos[1]);
            }
            //genero il campo
            f.bombsToField();
        }
        //se la partita non è ancora finita
        if(!f.isGameEnded()){
            //se il giocatore vuole rimuovere il tasto premuto
            if(!GameActivity.flag){
                replacePressed(B_id);
            }//se il giocatore vuole piazzare una bandierina
            else{
                f.placeFlag(B_id);
            }
            //se le condizioni di vittoria si sono verificate eseguo il metodo win
            if(winController() && !f.isGameEnded()){
                win();
            }
        }
    }
    //in caso di vittoria fermo il cronometro, genero i dato che potrebbero venir salvati e blocco i bottoni nel loro stato attuale
    private void win() {
        chr.stop();
        MyData gameData = new MyData((long) chr.getSeconds(), f.getDifficulty());
        f.endGame();
        f.setNotClickable();
        ga.win(gameData);
    }
    //in caso di sconfitta mostro tutto il campo e blocco i bottoni nel loro stato attuale
    private void lose() {
        f.endGame();
        f.setNotClickable();
        f.showField();
    }
    //controllo lo stato del campo per sapere se la partita è stata vinta o meno
    private boolean winController() {
        for(int i = 0; i< Field.DIM; i++){
            for(int j = 0; j< Field.DIM; j++){
                if((f.isNotPressed(i,j) || f.isFlag(i,j)) && !f.isBomb(i,j)){
                    return false;
                }
            }
        }
        return true;
    }
    //rimpiazzio il bottone che è stato premuto (con id = B_id) con l'immagine corrispondente
    public void replacePressed(int B_id) {
        int [] pos = f.getButtonCoordinates(B_id);
        if(!f.isFlag(pos[0],pos[1])){
            if(f.isBomb(pos[0],pos[1])){
                lose();
            }else{
                f.replaceElement(B_id);
            }
        }
    }
}
