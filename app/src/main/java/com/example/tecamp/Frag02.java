package com.example.tecamp;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tecamp.decorators.EventDecorator;
import com.example.tecamp.decorators.HighlightWeekendsDecorator;
import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.ui.main.PlaceholderFragment;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;

//カレンダー
public class Frag02 extends Fragment
        implements OnDateSelectedListener, OnMonthChangedListener {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("EEE曜日, yyyy年 MMM d日 ");
    private static final String TAG = "Frag02";

    private TabLayout tabLayout;

    @BindView(R.id.calendarView_1_tab)
    MaterialCalendarView widget;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag02_layout, container, false);
        ButterKnife.bind(this, view);
        widget.setOnDateChangedListener(this);
        widget.setOnMonthChangedListener(this);

        //表示設定
        widget.setHeaderTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setDateTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setWeekDayTextAppearance(R.style.TextAppearance_AppCompat_Large);
        widget.setTileSize(LinearLayout.LayoutParams.MATCH_PARENT);

        CalendarDay today = CalendarDay.today();
        MonthUpdate(today.getYear(), today.getMonth());


        return view;
    }

    private void MonthUpdate(int year, int month) {
        widget.removeDecorators();
        //土曜日　日曜日
        widget.addDecorator(new HighlightWeekendsDecorator());


        //DAY選択出来ない　設定
        widget.addDecorator(new PrimeDayDisableDecorator());

        //毎月スクリーン　更新
        HashMap<String, String> getMonthData = DataCenter.pData.getRoomsOneMonth(year, month);
        //Log.e(TAG, "onCreateView: getRoomsOneMonth:" + getMonthData.toString());

        ArrayList<CalendarDay> datesArray = new ArrayList<>();
        for (HashMap.Entry<String, String> entry : getMonthData.entrySet()) {
            datesArray.clear();
            //Log.e(TAG, "onCreateView: key value :" + entry.getKey() + " : " + entry.getValue());

            int y = Tools.dataGetYear(entry.getKey());
            int m = Tools.dataGetMonth(entry.getKey());
            int d = Tools.dataGetDay(entry.getKey());

            LocalDate localDate = LocalDate.of(y, m, d);
            CalendarDay singleDay = CalendarDay.from(localDate);
            datesArray.add(singleDay);
            widget.addDecorator(new EventDecorator(datesArray, "" + entry.getValue()));
        }
    }

    @Override
    public void onDateSelected(
            @NonNull MaterialCalendarView widget,
            @NonNull CalendarDay date,
            boolean selected) {
        Log.e(TAG, "onDateSelected: " + (selected ? FORMATTER.format(date.getDate()) : "選択ください"));
        //Log.e(TAG, "onDateSelected: " + FORMATTER.format(date.getDate()));
    }


    @Override
    public void onMonthChanged(MaterialCalendarView widget, CalendarDay date) {
        try {
            MonthUpdate(date.getYear(), date.getMonth());
        } catch (Exception e) {
            Log.e(TAG, "onMonthChanged: ", e);
        }
    }


    //選択できないDAY
    private static class PrimeDayDisableDecorator implements DayViewDecorator {

        @Override
        public boolean shouldDecorate(final CalendarDay day) {
            return PRIME_TABLE[day.getDay()];
        }

        @Override
        public void decorate(final DayViewFacade view) {

            view.setDaysDisabled(true);
        }

        //trueは選択できる。falseは表示選択できません。
        private static boolean[] PRIME_TABLE = {
                true,  // 0?
                true,
                true, // 2
                true, // 3
                true,
                true, // 5
                true,
                true, // 7
                true,
                true,
                true,
                true, // 11
                true,
                true, // 13
                true,
                true,
                true,
                true, // 17
                true,
                true, // 19
                true,
                true,
                true,
                true, // 23
                true,
                true,
                true,
                true,
                true,
                true, // 29
                true,
                true, // 31
                true,
                true,
                true, //PADDING
        };
    }
}

//特定日期 按钮 end