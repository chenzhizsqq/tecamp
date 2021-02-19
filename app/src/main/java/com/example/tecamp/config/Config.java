package com.example.tecamp.config;

import java.util.ArrayList;
import java.util.HashMap;

public class Config {

    //SharedPreferencesアプリの設定値

    //LoginActivityの設定値
    public static final String SharedPreferences_Login = "LoginSP";

    //Frag01の設定値
    public static final String SharedPreferences_Frag01 = "Frag01";

    //Frag05の設定値
    public static final String SharedPreferences_Frag05 = "Frag05";


    //public static final String LoginUrl = "https://ssl.ethp.net/EthpJson.aspx";
    public static final String LoginUrl = "http://192.168.18.126/EthpJson.aspx";
    //public static final String LoginUrl = "http://192.168.18.173/EthpJson.aspx";

    //Jsonデータ更新時間
    public static final long DataUpdateTime = 60000;    //60000では、毎６０秒更新一回

    /*sample getData json
        {"app":"EtCampGetData", "token":"202011291352391050000000090010000000000000010125",
        "device":"ios", "lasttime":""}
        {"app":"EtCampGetData", "token":"202011291352391050000000090010000000000000010125",
        "device":"ios", "lasttime":"20201201152359"}
     */

    public static String lastToken = "";        //EtCampGetDataで"token"
    public static String jsonGetLastTime = "";  //EtCampGetDataで"lasttime"


}
