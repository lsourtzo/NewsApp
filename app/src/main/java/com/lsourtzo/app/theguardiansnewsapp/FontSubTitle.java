package com.lsourtzo.app.theguardiansnewsapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by lsourtzo on 15/06/2017.
 */

public class FontSubTitle extends android.support.v7.widget.AppCompatTextView {
    public FontSubTitle(Context context) {
        super(context);
        setFont();
    }
    public FontSubTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public FontSubTitle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Black.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}