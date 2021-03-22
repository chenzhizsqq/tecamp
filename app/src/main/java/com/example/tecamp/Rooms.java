package com.example.tecamp;

import android.util.Log;

import java.util.HashMap;
import java.util.Map;

public class Rooms {
    private static final String TAG = "Rooms";
    private static int mSelectRoom = -1;


    //room siteid, sitename
    public static HashMap<Integer, String> mapRoomNames;


    public Rooms() {
        mSelectRoom = -1;
    }


    public static String getRoomName(int roomNum) {
        boolean isFindKey = false;
        for (Map.Entry<Integer, String> entry : mapRoomNames.entrySet()) {
            //Log.e(TAG, "getRoomName getKey: "+entry.getKey() );
            if (entry.getKey() == roomNum) {
                isFindKey = true;
                break;
            }
        }

        if (isFindKey) {
            try {
                return mapRoomNames.get(roomNum);
            } catch (Exception e) {
                Log.e(TAG, "getRoomName: ", e);
            }
        }
        return "未確定";
    }
}
