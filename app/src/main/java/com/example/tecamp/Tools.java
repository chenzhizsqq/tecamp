package com.example.tecamp;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.Calendar;
import java.util.Date;

public class Tools {
    private static final String TAG = "Tools";

    //20200101 to 2020-01-01
    static String dataChange(String _data, String _insert) {
        String rStr = _data;
        StringBuilder sb = new StringBuilder(rStr);
        sb.insert(6, _insert);
        sb.insert(4, _insert);
        rStr = sb.toString();
        return rStr;
    }

    public static String SrcContent(String src, int maxLength) {
        String rString = src;
        if (rString.length() > maxLength) {
            rString = src.substring(0, maxLength);
            rString += "...";
        }
        return rString;
    }


    static int dataGetYear(String _data) {
        if(_data.isEmpty())return 0;
        String str = _data.substring(0, 4);
        return Integer.parseInt(str);
    }

    static int dataGetMonth(String _data) {
        if(_data.isEmpty())return 0;
        String str = _data.substring(4, 6);
        return Integer.parseInt(str);
    }


    static int dataGetDay(String _data) {
        if(_data.isEmpty())return 0;
        String str = _data.substring(6, 8);
        return Integer.parseInt(str);
    }


    static int getDiffDays(Calendar calendar1, Calendar calendar2) {
        //==== ミリ秒単位での差分算出 ====//
        long diffTime = calendar1.getTimeInMillis() - calendar2.getTimeInMillis();


        //==== 日単位に変換 ====//
        int MILLIS_OF_DAY = 1000 * 60 * 60 * 24;
        int diffDays = (int)(diffTime / MILLIS_OF_DAY);

        return diffDays;
    }

    /*
    * 二つの日の違いの日数
    * */
    public static int differentDays(Date date1, Date date2)
    {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        int day1= cal1.get(Calendar.DAY_OF_YEAR);
        int day2 = cal2.get(Calendar.DAY_OF_YEAR);

        int year1 = cal1.get(Calendar.YEAR);
        int year2 = cal2.get(Calendar.YEAR);
        if(year1 != year2)
        {
            int timeDistance = 0 ;
            for(int i = year1 ; i < year2 ; i ++)
            {
                if(i%4==0 && i%100!=0 || i%400==0)
                {
                    timeDistance += 366;
                }
                else
                {
                    timeDistance += 365;
                }
            }

            return timeDistance + (day2-day1) ;
        }
        else
        {
            return day2-day1;
        }
    }

    /**
     * キーボードを閉じる
    * */
    public static void hideKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && imm.isActive()) {
            imm.hideSoftInputFromWindow(v.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
