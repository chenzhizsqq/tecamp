package com.example.tecamp.sql;

import com.example.tecamp.JsonControl;
import com.example.tecamp.Rooms;

public class DataCenter {
    private static final String TAG = "DataCenter";
    public static JsonControl pData = new JsonControl();

    public static void UpdateData() {
        //Jsonデータを貰います。 begin
        //DataCenter.pData.dbInit( getActivity());
        JsonControl.mMakeJsonGetDataNext = true;
        while (JsonControl.mMakeJsonGetDataNext) {
            DataCenter.pData.MakeJsonGetData();
            if(JsonControl.mMakeJsonGetDataNext){

                DataCenter.pData.Json2SqlOrder();
            }
        }
        DataCenter.pData.Json2SqlSite();
        DataCenter.pData.updateSiteRoomDataArray();

        Rooms.mapRoomNames=DataCenter.pData.getSqlRoomNames();

    }
}
