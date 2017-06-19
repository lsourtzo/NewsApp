package com.lsourtzo.app.theguardiansnewsapp;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by lsourtzo on 15/06/2017.
 */

public class FontRegular extends android.support.v7.widget.AppCompatTextView {
    public FontRegular(Context context) {
        super(context);
        setFont();
    }
    public FontRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        setFont();
    }
    public FontRegular(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setFont();
    }

    private void setFont() {
        Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/Merriweather-Regular.ttf");
        setTypeface(font, Typeface.NORMAL);
    }
}
