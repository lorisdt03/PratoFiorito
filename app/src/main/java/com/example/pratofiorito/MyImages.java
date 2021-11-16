package com.example.pratofiorito;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;

import androidx.appcompat.content.res.AppCompatResources;

public class MyImages {

    private final Drawable[] numbers = new Drawable[9];
    private final Drawable flag;
    private final Drawable wrongFlag;
    private final Drawable button;
    private final Drawable bomb;
    private final Drawable border;
    private final int B_SIZE;

    //creo tutte le immagini che mi serviranno all'interno del programma
    MyImages(Context context){

        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        B_SIZE = size.x/11;
        numbers[0] = scaleDrawable(context, R.drawable.empty);
        numbers[1] = scaleDrawable(context, R.drawable.n1);
        numbers[2] = scaleDrawable(context, R.drawable.n2);
        numbers[3] = scaleDrawable(context, R.drawable.n3);
        numbers[4] = scaleDrawable(context, R.drawable.n4);
        numbers[5] = scaleDrawable(context, R.drawable.n5);
        numbers[6] = scaleDrawable(context, R.drawable.n6);
        numbers[7] = scaleDrawable(context, R.drawable.n7);
        numbers[8] = scaleDrawable(context, R.drawable.n8);

        flag = scaleDrawable(context, R.drawable.placeable_flag);
        wrongFlag = scaleDrawable(context, R.drawable.wrong_flag);
        button =  scaleDrawable(context, R.drawable.button);
        bomb = scaleDrawable(context, R.drawable.bomb);
        border = AppCompatResources.getDrawable(context,R.drawable.border);
    }
    //scalo l'oggeto drawable con id "id" alle dimensioni size*size
    private Drawable scaleDrawable(Context context, int id) {
        Drawable d = AppCompatResources.getDrawable(context,id);
        int size = getButtonSize(context);
        return  new ScaleDrawable(d, 0, size, size).getDrawable();
    }
    //restituisco la dimensione in px dei bottoni
    public int getButtonSize(Context context){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,B_SIZE,context.getResources().getDisplayMetrics());
    }
    //restituisco se 2 drawables siano identici o meno
    private boolean equals(Drawable d1, Drawable d2){
        Bitmap b1 = ((BitmapDrawable)d1).getBitmap();
        Bitmap b2 = ((BitmapDrawable)d2).getBitmap();
        return b1.equals(b2);
    }
    //restituisco se l'immagine del bottone b sia una bandierina
    public boolean isFlag(ImageButton b){
        return isFlag(b.getDrawable());
    }
    //restituisco se l'immagine d sia una bandierina
    public boolean isFlag(Drawable d){
        return equals(d,flag);
    }
    //restituisco se l'immagine del bottone b sia un bottone
    public boolean isButton(ImageButton b){
        return isButton(b.getDrawable());
    }
    //restituisco se l'immagine d sia un bottone
    public boolean isButton(Drawable d){
        return equals(d,button);
    }
    //restituisco se l'immagine del bottone b sia una bomba
    public boolean isBomb(ImageButton b){
        return isBomb(b.getDrawable());
    }
    //restituisco se l'immagine d sia una bomba
    public boolean isBomb(Drawable d){
        return equals(d,bomb);
    }
    //restituisco se l'immagine del bottone b sia l'immagine di un vuoto
    public boolean isEmpty(ImageButton b){
        return isEmpty(b.getDrawable());
    }
    //restituisco se l'immagine d sia l'immagine di un vuoto
    public boolean isEmpty(Drawable d){
        return equals(d,numbers[0]);
    }
    //restituisco l'immagine che rappresenta il numero pos (un vuoto in caso di 0)
    public Drawable getNumber(int pos){
        return numbers[pos];
    }
    //restituisco l'immagine di una bandierina
    public Drawable getFlag() {
        return flag;
    }
    //restituisco l'immagine di una bomba
    public Drawable getBomb() {
        return bomb;
    }
    //restituisco l'immagine di un bottone
    public Drawable getButton() {
        return button;
    }
    //restituisco l'immagine di una bandierina crociata
    public Drawable getWrongFlag() {
        return wrongFlag;
    }
    //restituisco l'immagine di un bordo
    public Drawable getBorder() {
        return border;
    }
}
