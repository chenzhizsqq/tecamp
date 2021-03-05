package com.example.tecamp.sql;

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
    public static JsonControl pData = new JsonControl();
    private static Timer timer;

    public static String UpdateData() {
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

            Log.e(TAG, "UpdateData: updateDataTimes:" + updateDataTimes);
            Log.d(TAG, "UpdateData: last status:" + status);
        }catch (Exception e){
            Log.e(TAG, "UpdateData: ",e );
        }
        return status;
    }

    public static void timeCreate() {
        long count, second, minute;
        // タイマーが走っている最中にボタンをタップされたケース
        if (null != timer) {
            timer.cancel();
            timer = null;
        }

        // Timer インスタンスを生成
        timer = new Timer();

        // TimerTask インスタンスを生成
        CountUpTimerTask timerTask = new CountUpTimerTask();

        second = 1000;  //1000では、遅延１秒
        timer.schedule(timerTask, second, Config.DataUpdateTime);
    }

    private final static Handler handler = new Handler(Looper.getMainLooper());

    //データ更新task
    static class CountUpTimerTask extends TimerTask {
        @Override
        public void run() {
            // handlerを使って処理をキューイングする
            handler.post(new Runnable() {
                public void run() {
                    Log.d(TAG, "CountUpTimerTask class  毎60秒更新一回");

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            String status = DataCenter.UpdateData();
                            Log.d(TAG, "CountUpTimerTask status:" + status);

                        }
                    }).start();
                }
            });
        }
    }


}
