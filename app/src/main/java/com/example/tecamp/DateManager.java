package com.example.tecamp;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateManager {

    private static final String TAG = "DateManager";
    private Calendar calendar;


    public DateManager() {
        calendar = Calendar.getInstance();
    }

    public Date getDate(){
        Date date = calendar.getTime();
        return date;
    }

    public void resetNow() {
        calendar = Calendar.getInstance();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public Calendar addCalendar(int apartDays) {
        calendar.add(Calendar.DAY_OF_YEAR, apartDays);
        return calendar;
    }

    public int getDay() {
        return calendar.get(Calendar.DATE);
    }

    public String getDayS(){
        return getDay() > 9 ?""+getDay():"0"+getDay();
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH) + 1;
    }

    public String getMonthS(){
        return getMonth() > 9 ?""+getMonth():"0"+getMonth();
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public String getYearS() {
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    //データのday更新
    public void addDay(int apartDays) {
        calendar = addCalendar(apartDays);
    }

    public void setDate(int year, int month, int day) {
        calendar.set(year, month - 1, day);
    }

    public void setDate(String YYYYMMDD) {
        //Log.e(TAG, "setDate: YYYYMMDD:"+YYYYMMDD );
        int year=Integer.parseInt(YYYYMMDD.substring(0,4));
        int month=Integer.parseInt(YYYYMMDD.substring(4,6));
        int day=Integer.parseInt(YYYYMMDD.substring(6,8));
        /*Log.e(TAG, "setDate: year:"+year );
        Log.e(TAG, "setDate: month:"+month );
        Log.e(TAG, "setDate: day:"+day );*/
        calendar.set(year, month - 1, day);
    }

    public String getYMDHMS() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);

        String hour_s = hour > 9 ? "" + hour : "0" + hour;
        String minute_s = minute > 9 ? "" + minute : "0" + minute;
        String second_s = second > 9 ? "" + second : "0" + second;

        return ""+getYear() + getMonthS() + getDayS() + hour_s + minute_s + second_s;
    }
    public String getYMD() {

        return ""+getYear() + getMonthS() + getDayS() ;
    }
    public String getYMD(String add) {
        String str = String.format(
                Locale.US,
                "%d"+add+"%s"+add+"%s",
                getYear(),
                getMonthS(),
                getDayS()
        );
        return str;
    }
}
