package com.example.tecamp;

import java.util.Calendar;

public class DateManager {

    private Calendar calendar;


    public DateManager() {
        calendar = Calendar.getInstance();
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

    //データのday更新
    public void addDay(int apartDays) {
        calendar = addCalendar(apartDays);
    }

    public void setDate(int year, int month, int day) {
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
}
