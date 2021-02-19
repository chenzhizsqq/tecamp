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
        String status = "";
        //Jsonデータを貰います。 begin
        //DataCenter.pData.dbInit( getActivity());
        JsonControl.pJsonGetDataNext = true;
        while (JsonControl.pJsonGetDataNext) {
            status = DataCenter.pData.MakeJsonGetData();
            if (JsonControl.pJsonGetDataNext) {

                DataCenter.pData.Json2SqlOrder();
            }
        }
        DataCenter.pData.Json2SqlSite();
        DataCenter.pData.updateSiteRoomDataArray();

        Rooms.mapRoomNames = DataCenter.pData.getSqlRoomNames();

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
