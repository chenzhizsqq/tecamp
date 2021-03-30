package com.example.tecamp;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.example.tecamp.config.Config;
import com.example.tecamp.sql.DataCenter;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.os.Looper;

import com.example.tecamp.ui.main.SectionsPagerAdapter;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity{

    private static final String TAG = "MainActivity";


    @SuppressLint("NonConstantResourceId")

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DataCenter.setContext(this);
        timeCreate();


        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());


        ViewPager viewPager = findViewById(R.id.view_pager);

        sectionsPagerAdapter.add(new Frag05(viewPager),"予約一覧 全部");
        sectionsPagerAdapter.add(new Frag04(viewPager),"電話受付");;
        sectionsPagerAdapter.add(new FragEachDay(viewPager),"予約一覧 毎日");
        sectionsPagerAdapter.add(new OrderCalendar(viewPager),"カレンダー");
        //sectionsPagerAdapter.add(new Frag03(viewPager),"予約検索");
        sectionsPagerAdapter.add(new FragSelect(viewPager),"予約検索");
        /*
        sectionsPagerAdapter.add(new Frag05(viewPager),"予約一覧list");
        sectionsPagerAdapter.add(new FragEachDay(viewPager),"予約一覧E");
        sectionsPagerAdapter.add(new Frag04(viewPager),"電話受付");
        sectionsPagerAdapter.add(new Frag01(viewPager),"予約一覧");
        sectionsPagerAdapter.add(new OrderCalendar(viewPager),"カレンダー");
        sectionsPagerAdapter.add(new Frag03(viewPager),"予約検索");*/



        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        //最初表示Frag
        viewPager.setCurrentItem(4);
    }

    private Timer timer;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private void timeCreate() {
        long  second;
        if (null != timer) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();

        // TimerTask インスタンスを生成
        CountUpTimerTask timerTask = new CountUpTimerTask();

        second = 60000;  //60000では、遅延１分
        timer.schedule(timerTask, second, Config.DataUpdateTime);
    }
    //データ更新task
    class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                        //表示画面更新
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                DataCenter.UpdateData();
                            }
                        }).start();
                }
            });
        }
    }
}

