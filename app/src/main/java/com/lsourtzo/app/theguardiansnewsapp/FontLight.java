package com.lsourtzo.app.theguardiansnewsapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by lsourtzo on 15/06/2017.
 */

public class FontLight extends android.support.v7.widget.AppCompatTextView {
    public FontLight(Context context) {
        super(context);
        setFont();
    }
    public FontLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public FontLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Light.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
