package com.example.pratofiorito;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

public class EnterListener implements TextView.OnEditorActionListener {

    private final WinActivity wa;

    //salvo come variabile della classe il context in cui è stato implementato il listener
    public EnterListener(WinActivity wa) {
        this.wa = wa;
    }

    //quando viene premuto invio nascondo la tastiera
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        View view = wa.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) wa.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        return true;
    }
}
