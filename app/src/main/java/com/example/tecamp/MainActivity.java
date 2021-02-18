package com.example.tecamp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.tecamp.ui.main.SectionsPagerAdapter;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindView;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";

    private  static ViewPager viewPager;


    @SuppressLint("NonConstantResourceId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());


        ViewPager viewPager = findViewById(R.id.view_pager);

        sectionsPagerAdapter.add(new Frag05(viewPager),"予約一覧list");
        sectionsPagerAdapter.add(new Frag04(viewPager),"電話受付");
        sectionsPagerAdapter.add(new Frag01(viewPager),"予約一覧");
        sectionsPagerAdapter.add(new Frag02(viewPager),"カレンダー");
        sectionsPagerAdapter.add(new Frag03(viewPager),"予約検索");



        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);


    }


}

