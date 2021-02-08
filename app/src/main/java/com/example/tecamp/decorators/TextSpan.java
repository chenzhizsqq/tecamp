package com.example.tecamp.decorators;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.style.LineBackgroundSpan;

public class TextSpan implements LineBackgroundSpan {

    private final String text;
    private int bottomHeight;
    private int color;

    public TextSpan(String text,int bottomHeight,int color) {
        this.text = text;
        this.bottomHeight = bottomHeight;
        this.color = color;
    }

    @Override
    public void drawBackground(Canvas c, Paint p, int left, int right, int top, int baseline, int bottom, CharSequence txt, int start, int end, int lnum) {
        Paint paint2 = new Paint();
        //paint2.setColor(Color.BLUE);
        paint2.setColor(this.color);
        paint2.setTextSize(p.getTextSize() * 0.75f);
        paint2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        float w = paint2.measureText(text) / 2;
        float textSize = paint2.getTextSize();
        paint2.setTextAlign(Paint.Align.CENTER);
        c.drawRect(0, 50, 50, 50, p);
        c.drawText(String.valueOf(text), (left + right) / 2, bottom + this.bottomHeight, paint2);
    }
}
