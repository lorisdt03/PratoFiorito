package com.example.pratofiorito;

import androidx.annotation.NonNull;

public class MyData {

    private final long score;
    private final long time;
    private final int difficulty;
    private String name;

    //creo un oggetto di tipo MyData default
    MyData(){
        time = 0;
        difficulty = 0;
        score = score();
        name = "----------";
    }
    //creo un oggetto di tipo MyData con la difficoltà e il tempo passati
    MyData(long time, int difficulty){
        this.difficulty=difficulty;
        this.time = time;
        score = score();
    }
    //creo un oggetto di tipo MyData con la difficoltà, il tempo e il nome che sono stati passati come stringa
    MyData(String s){
        time= Integer.parseInt(s.split("_")[0]);
        difficulty = Integer.parseInt(s.split("_")[1]);
        try{
            name = s.split("_")[2];
        }catch (Exception e){
            name = "----------";
        }
        score = score();
    }
    MyData(String name, int difficulty, int time){
        this.time = time;
        this.difficulty = difficulty;
        this.name = name;
        score = score();
    }
    //restituisco una versione convertita a stringa di un oggetto d tipo MyData
    @NonNull
    @Override
    public String toString() {
        return time +"_" + difficulty+"_"+name;
    }
    //calcolo il punteggio da tempo impiegato e difficoltà
    private long score(){
        if(7200-time>0){
            return (long)((7200-time)*difficulty*1.75);
        }
        return 0;
    }
    //restituisco il punteggio
    public long getScore() {
        return score;
    }
    //restituisco il punteggio come stringa
    public String getScoreStr(){
        return Long.toString(score);
    }
    //restituisco il tempo
    public long getTime() {
        return time;
    }
    //restituisco il tempo come stringa
    public String getTimeStr(){
        return time + "s";
    }
    //restituisco la difficoltà
    public int getDifficulty() {
        return difficulty;
    }
    //restituisco la stringa corrispondente a ogni difficoltà
    public String getDifficultyStr(){
        switch (difficulty){
            case Field.EASY:
                return "FACILE";
            case Field.NORMAL:
                return "NORMALE";
            case Field.HARD:
                return "DIFFICILE";
        }
        return null;
    }
    //imposto come nome il nome passato
    public void setName(String name){
        this.name = name;
    }
    //restituisco il nome
    public String getName(){
        return name;
    }
}
