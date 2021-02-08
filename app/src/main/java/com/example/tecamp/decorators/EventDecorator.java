package com.example.tecamp.decorators;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.StyleSpan;
import android.graphics.Typeface;
import android.text.style.RelativeSizeSpan;
import android.text.style.ForegroundColorSpan;

/**
 * Decorate several days with a dot
 */
public class EventDecorator implements DayViewDecorator {

    private int dayColor;
    private int eventColor;
    private HashSet<CalendarDay> dates;
    private String text;
    private float sizeSpan;
    private int bottomHeight;

    public void setHighlightDrawable(Drawable highlightDrawable) {
        this.highlightDrawable = highlightDrawable;
    }

    private Drawable highlightDrawable;

    public EventDecorator(Collection<CalendarDay> dates,String text) {
        this.dayColor=Color.BLUE;
        this.eventColor = Color.RED;
        this.dates = new HashSet<>(dates);
        this.text = text;
        this.sizeSpan = 1.0f;
        this.bottomHeight = 30;

    }

    public EventDecorator(int dayColor,int eventColor, Collection<CalendarDay> dates, String text, float sizeSpan,int bottomHeight) {
        this.dayColor=dayColor;
        this.eventColor = eventColor;
        this.dates = new HashSet<>(dates);
        this.text = text;
        this.sizeSpan = sizeSpan;
        this.bottomHeight = bottomHeight;

    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.setDaysDisabled(false);
        //view.addSpan(new DotSpan(10, color));

        //view.addSpan(new TextSpan(text,this.bottomHeight, Color.RED));
        view.addSpan(new TextSpan(text,this.bottomHeight, this.eventColor));
        view.addSpan(new ForegroundColorSpan(this.dayColor));
        view.addSpan(new StyleSpan(Typeface.BOLD));
        view.addSpan(new RelativeSizeSpan(this.sizeSpan));
        if(this.highlightDrawable!=null){
            view.setBackgroundDrawable(this.highlightDrawable);

        }
    }
}
