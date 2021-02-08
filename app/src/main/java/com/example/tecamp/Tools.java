package com.example.tecamp;

import android.util.Log;

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
}
