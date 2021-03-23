package com.example.tecamp.sql;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.example.tecamp.JsonControl;
import com.example.tecamp.Rooms;
import com.example.tecamp.config.Config;

import java.util.Timer;
import java.util.TimerTask;

public class DataCenter {
    private static final String TAG = "DataCenter";
    public static JsonControl pData ;
    private static Timer timer;

    public static void setContext(Context context){
        pData = new JsonControl(context);
        Rooms.mapRoomNames = DataCenter.pData.getSqlRoomNames();
    }

    public static String UpdateData() {
        Log.e(TAG, "UpdateData: begin" );
        int updateDataTimes = 0;
        String status = "";
        //Jsonデータを貰います。 begin
        try {

            status = DataCenter.pData.MakeJsonGetData();
            String resultOrderList="";
            while (status.equals("0")) {

                DataCenter.pData.Json2SqlOrder();
                resultOrderList=DataCenter.pData.getResultOrderList();
                updateDataTimes += 1;

                if(resultOrderList.equals(DataCenter.pData.getResultOrderList())){
                    Log.d(TAG, "UpdateData: resultOrderList==DataCenter.pData.getResultOrderList() after continue" );
                    break;
                }else{

                    status = DataCenter.pData.MakeJsonGetData();
                }

            }


            //DataCenter.pData.updateSiteRoomDataArray();

            //DataCenter.pData.Json2SqlSite();
            Rooms.mapRoomNames = DataCenter.pData.getSqlRoomNames();

            Log.d(TAG, "UpdateData: updateDataTimes:" + updateDataTimes);
            Log.d(TAG, "UpdateData: last status:" + status);
        }catch (Exception e){
            Log.e(TAG, "UpdateData: ",e );
        }
        return status;
    }


}
