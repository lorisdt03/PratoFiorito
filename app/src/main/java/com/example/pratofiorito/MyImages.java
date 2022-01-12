package com.example.pratofiorito;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;

import androidx.appcompat.content.res.AppCompatResources;

public class MyImages {

    private final Drawable[] numbers = new Drawable[9];
    private final Drawable flag;
    private final Drawable wrongFlag;
    private final Drawable button;
    private final Drawable bomb;
    private final int B_SIZE;

    //creo tutte le immagini che mi serviranno all'interno del programma
    MyImages(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        B_SIZE = displayMetrics.widthPixels / (Field.DIM+1);

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
        button = scaleDrawable(context, R.drawable.button);
        bomb = scaleDrawable(context, R.drawable.bomb);
    }

    //scalo l'oggeto drawable con id "id" alle dimensioni size*size
    private Drawable scaleDrawable(Context context, int B_id) {
        int size = getButtonSize();
        Bitmap bMap = BitmapFactory.decodeResource(context.getResources(), B_id);
        Bitmap bMapScaled = Bitmap.createScaledBitmap(bMap, size, size, true);
        return new BitmapDrawable(context.getResources(), bMapScaled);
    }

    //restituisco la dimensione in px dei bottoni
    public int getButtonSize() {
        return B_SIZE;
    }

    //restituisco l'immagine che rappresenta il numero pos (un vuoto in caso di 0)
    public Drawable getNumber(int pos) {
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
}
