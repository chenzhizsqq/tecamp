package com.example.tecamp;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tecamp.config.Config;
import com.example.tecamp.sql.DataCenter;
import com.example.tecamp.sql.OrderListSql;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class JsonControl {
    private static final String TAG = "JsonControl";

    private SQLiteDatabase mDB = null;
    OrderListSql orderListSql;

    public JsonControl() {
        orderListSql = new OrderListSql(null);
        mDB = orderListSql.getWritableDatabase();
    }

    public String EtCampGetDataJson() {

        String rJsonLoginGet = "{" +
                "\"app\":\"EtCampGetData\"" +
                ", \"token\":\"" + Config.lastToken + "\"" +
                ", \"device\":\"android\"" +
                ", \"lasttime\":\"" + Config.jsonGetLastTime +
                "\"}";
        return rJsonLoginGet;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    private static String uid = "";
    private static String password = "";
    private static String mLoginToken = "";


    private JSONObject mJsonObjLogin = new JSONObject();

    public JSONObject getJsonObjLogin() {
        return mJsonObjLogin;
    }

    public void setJsonObject(JSONObject mJsonObject) {
        this.mJsonObject = mJsonObject;
    }

    JSONObject mJsonObject = new JSONObject();

    public JSONObject getJsonObject() {
        return mJsonObject;
    }


    static public String getLoginJson(String app, String uid, String password, String device) {
        return "{\"app\":\"" + app +
                "\", \"uid\":\"" + uid +
                "\", \"password\":\"" + password +
                "\", \"device\":\"" + device +
                "\"}";
    }

    static public String getDataJson(String app, String token, String device, String lastTime) {
        return "{\"app\":\"" + app +
                "\", \"token\":\"" + token +
                "\", \"device\":\"" + device +
                "\",\"lasttime\":\"" + lastTime +
                "\"}";
    }

    public String getLoginToken() {
        return this.mLoginToken;
    }

    public void setLoginToken(String loginToken) {
        Log.d(TAG + ":setLoginToken", loginToken);
        mLoginToken = loginToken;
    }

    public String LoginInit(String editTextTextEmailAddress, String editTextTextPassword) {

        String status = "";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String mJson = getLoginJson(
                "EtCampLogin"
                , editTextTextEmailAddress
                , editTextTextPassword
                , "android");
        String url = Config.LoginUrl;

        RequestBody body = RequestBody.create(JSON, mJson);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String getResult = "";
        try {
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                getResult = response.body().string();
                mJsonObjLogin = new JSONObject(getResult);
                Log.d(TAG, "LoginInit: getResult:" + getResult);
                status = mJsonObjLogin.getString("status");
                if (mJsonObjLogin.getString("status").equals("0")) {
                    String mGetJsonObjLogin = getJsonObjLogin().toString();
                    String token = getJsonObjLogin().getJSONObject("result").getString("token");
                    Config.lastToken = token;

                    setLoginToken(token);

                    //return true;
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "LoginInit: ", e);
        }

        return status;
        //return false;
    }

    /*final static OkHttpClient client = new OkHttpClient();
    final static MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    static Response response = null;*/
    //予約データ　Jsonデータを貰います。
    //public static boolean pJsonGetDataNext = true;
    //public static String mGetLastJsonObjGetDate = "";


    public String MakeJsonGetData() {
        String status = "";
        OkHttpClient client = new OkHttpClient();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        String jsonString = DataCenter.pData.EtCampGetDataJson();
        String url = Config.LoginUrl;

        RequestBody body = RequestBody.create(JSON, jsonString);
        //Log.e(TAG, "MakeJsonGetData jsonString: "+jsonString );
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        String getResult = "";
        try {
            Response response = null;
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                getResult = response.body().string();

                setJsonObject(new JSONObject(getResult));

                Log.d(TAG, "MakeJsonGetData: getResultServerTime:" + getResultServerTime());

                if (!Config.jsonGetLastTime.equals(getResultServerTime())) {
                    Config.jsonGetLastTime = getResultServerTime();
                    Log.d(TAG, "MakeJsonGetData: Config.jsonGetLastTime :" + Config.jsonGetLastTime);
                } else {
                    return "-1";
                }
                status = getJsonObject().getString("status");
                //if (getJsonObject().getString("status").equals("0")) {
                /*if (status.equals("0")) {
                    Log.e(TAG, "MakeJsonGetData: status.equals(\"0\")" );
                }*/
                //Log.e(TAG, "MakeJsonGetData: getResult:" + getResult);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "MakeJsonGetData: ", e);
        }

        return status;
    }

    //orderlistのjson
    public int getOrderListLength() throws JSONException {
        return getJsonObject().getJSONObject("result").getJSONArray("orderlist").length();
    }

    public String getJSONArrayOrderList(int index, String name) throws JSONException {

        String r = "";
        try {
            r = getJsonObject().getJSONObject("result").getJSONArray("orderlist").getJSONObject(index).getString(name);
        } catch (Exception e) {
            Log.e(TAG, "getJSONArrayOrderList: index:" + index + " name:" + name);
            Log.e(TAG, "getJSONArrayOrderList: ", e);
        }
        return r;
    }


    public int getSiteListLength(int orderListIndex) throws JSONException {
        int r = 0;
        try {
            r = getJsonObject().getJSONObject("result")
                    .getJSONArray("orderlist").getJSONObject(orderListIndex)
                    .optJSONArray("sitelist").length();
        } catch (Exception e) {
            Log.e(TAG, "getSiteListLength: ", e);
        }
        return r;
    }

    //sitelistで、配列内の各変数の値
    public String getSiteListValue(int orderlistIndex, int sitelistIndex, String name) throws JSONException {
        String r = "";
        try {
            r = getJsonObject().getJSONObject("result")
                    .getJSONArray("orderlist").getJSONObject(orderlistIndex)
                    .optJSONArray("sitelist").getJSONObject(sitelistIndex).getString(name);
        } catch (Exception e) {
            Log.e(TAG, "getSiteListValue: ", e);
        }
        return r;
    }

    //sitelistのjson
    public int getJSONArraySiteListLength() throws JSONException {
        return 0;
    }

    public String getJSONArraySiteList(int index, String name) throws JSONException {

        return getJsonObject().getJSONObject("result").getJSONArray("sitelist").getJSONObject(index).getString(name);
    }


    //EtCampGetData でOrderListのjsonデータを貰います。
    public String getOrderListValue(int index, String name) throws JSONException {
        String rStrValue = "";
        try {
            rStrValue = getJSONArrayOrderList(index, name);
        } catch (Exception e) {
            Log.e(TAG, "getOrderListValue: ", e);
        }
        return rStrValue;
    }

    //EtCampGetData でSiteListのjsonデータを貰います。
    public String getSiteListJsonValue(int index, String name) throws JSONException {
        String rStrValue = "";
        try {
            rStrValue = getJSONArraySiteList(index, name);
        } catch (Exception e) {
            Log.e(TAG, "getSiteListJsonValue: ", e);
        }
        return rStrValue;
    }


    //sql insert

    public ArrayList<HashMap<String, String>> GetOrderListArrayList() {
        return mSimpleDataArrayList;
    }

    //簡単記録list
    private final ArrayList<HashMap<String, String>> mSimpleDataArrayList = new ArrayList<>();

    //「サイト数」詳細記録list
    public final ArrayList<HashMap<String, String>> mSiteDataArrayList = new ArrayList<>();

    //「サイト数」詳細記録SiteRoom
    private final ArrayList<HashMap<String, String>> mSiteRoomArrayList = new ArrayList<>();

    //「テスト」詳細記録list
    private final ArrayList<HashMap<String, String>> mTestDataArrayList = new ArrayList<>();


    private static final String[] mListName = {
            "firstymd",
            "days",
            "username",
            "count",
            "siteid",
            "way",
            "memo"
    };

    public int getSimpleDataArrayListSize() {
        return mSimpleDataArrayList.size();
    }

    public void testOrderListArrayList() throws JSONException {
        for (int i = 0; i < mSimpleDataArrayList.size(); i++) {

            for (int j = 0; j < mListName.length; j++) {

                Log.d(TAG, "testOrderListArrayList: getOrderListSqlValue(" + j + "," + mListName[j] + "):" + getSimpleData(i, mListName[j]));
            }
        }
    }


    public String getSimpleData(int index, String name) throws JSONException {
        String rStrValue = "";
        try {

            switch (name) {
                case "firstymd":
                    rStrValue = Tools.dataChange(mSimpleDataArrayList.get(index).get(name), "/");
                    break;
                case "tel":
                    String searchName1 = "tel_1";
                    String v_1 = mSimpleDataArrayList.get(index).get(searchName1);
                    String searchName2 = "tel_2";
                    String v_2 = mSimpleDataArrayList.get(index).get(searchName2);
                    String searchName3 = "tel_3";
                    String v_3 = mSimpleDataArrayList.get(index).get(searchName3);
                    rStrValue = v_1 + "-" + v_2 + "-" + v_3;
                    break;
                case "count":
                    int nTemp = 0;
                    String searchName_count_adult = "count_adult";
                    String sTemp = Objects.requireNonNull(mSimpleDataArrayList.get(index).get(searchName_count_adult));
                    int count_adult = 0;
                    if (!sTemp.isEmpty()) {
                        count_adult = Integer.parseInt(Objects.requireNonNull(mSimpleDataArrayList.get(index).get(searchName_count_adult)));
                    }

                    String searchName_count_child = "count_child";
                    sTemp = Objects.requireNonNull(mSimpleDataArrayList.get(index).get(searchName_count_child));
                    int count_child = 0;
                    if (!sTemp.isEmpty()) {
                        count_child = Integer.parseInt(Objects.requireNonNull(mSimpleDataArrayList.get(index).get(searchName_count_child)));
                    }
                    rStrValue = Integer.toString(count_adult + count_child);
                    if (count_child > 0) {
                        rStrValue = rStrValue + "(" + count_child + ")";
                    }
                    break;

                case "username":
                    String temp = mSimpleDataArrayList.get(index).get("username");
                    assert temp != null;
                    if (temp.isEmpty()) {
                        temp = "*";
                    }
                    rStrValue = temp;
                    break;

                default:
                    rStrValue = mSimpleDataArrayList.get(index).get(name);
                    break;
            }
        } catch (Exception e) {
            Log.e(TAG, "getSimpleData: ", e);
        }
        return rStrValue;
    }


    public void Json2SqlOrder() {
        try {
            if (getOrderListLength() > 0) {
            } else {
                return;
            }
            //OrderListのjson、SQLデータを入ります
            for (int j = 0; j < getOrderListLength(); j++) {

                ContentValues values = new ContentValues();
                for (int i = 0; i < OrderListSql.tableRowNames.length; i++) {
                    values.put(OrderListSql.tableRowNames[i], this.getOrderListValue(j, OrderListSql.tableRowNames[i]));
                }

                String sWhere = "orderid =" + this.getOrderListValue(j, "orderid");
                String testSql = "select orderid from " + OrderListSql.orderTableName + " where " + sWhere;
                int count = getSqlCount(testSql);
                if (count > 0) {

                    mDB.update(OrderListSql.orderTableName, values, sWhere, null);
                } else {

                    mDB.insert(OrderListSql.orderTableName, null, values);
                }

                values.clear();
            }

            for (int j = 0; j < getOrderListLength(); j++) {

                int siteListLength = getSiteListLength(j);

                for (int i = 0; i < siteListLength; i++) {
                    ContentValues values = new ContentValues();
                    values.put("orderid", this.getOrderListValue(j, "orderid"));
                    values.put("ordernum", this.getOrderListValue(j, "ordernum"));
                    for (int k = 0; k < OrderListSql.SiteListRowNames.length; k++) {
                        values.put(OrderListSql.SiteListRowNames[k], this.getSiteListValue(j, i, OrderListSql.SiteListRowNames[k]));
                    }

                    String sWhere = "orderid =" + this.getOrderListValue(j, "orderid") +
                            " and ymd=" + getSiteListValue(j, i, "ymd") +
                            " and subid=" + getSiteListValue(j, i, "subid");
                    String testSql = "select orderid from " + OrderListSql.SiteListTableName + " where " + sWhere;
                    int count = getSqlCount(testSql);
                    if (count > 0) {

                        mDB.update(OrderListSql.SiteListTableName, values, sWhere, null);
                    } else {

                        mDB.insert(OrderListSql.SiteListTableName, null, values);
                    }
                    values.clear();
                }

            }


        } catch (Exception e) {
            Log.e(TAG, "Json2SqlOrder: ", e);
        }
    }

    public void Json2SqlSite() {
        try {


            //SiteListのjson、SQLデータを入ります
            for (int j = 0; j < getJSONArraySiteListLength(); j++) {

                ContentValues values = new ContentValues();
                for (int i = 0; i < OrderListSql.SiteTableRowNames.length; i++) {
                    values.put(OrderListSql.SiteTableRowNames[i], this.getSiteListJsonValue(j, OrderListSql.SiteTableRowNames[i]));
                }

                String sWhere = "siteid =" + this.getSiteListJsonValue(j, "siteid");
                String testSql = "select siteid from " + OrderListSql.SiteTableName + " where " + sWhere;
                int count = getSqlCount(testSql);
                if (count > 0) {

                    mDB.update(OrderListSql.SiteTableName, values, sWhere, null);
                } else {

                    mDB.insert(OrderListSql.SiteTableName, null, values);
                }
                values.clear();
            }


        } catch (Exception e) {
            Log.e(TAG, "Json2SqlSite: ", e);
        }
    }

    public int getSqlCount(String sql) {
        int count = 0;
        Cursor cursor = null;
        try {
            cursor = mDB.rawQuery(sql, null);
            count = cursor.getCount();
        } catch (Exception e) {
            // this gets called even if there is an exception somewhere above
            Log.e(TAG, "getSqlCount: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
        return count;
    }

    public ArrayList<Integer> SqlGetIntArray(String sql) {
        ArrayList<Integer> IntArray = null;
        Cursor cursor = null;
        try {

            IntArray = new ArrayList<>();
            cursor = mDB.rawQuery(sql, null);
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                IntArray.add(cursor.getInt(0));
                isEof = cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(TAG, "SqlGetStringArray: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
        return IntArray;
    }

    public ArrayList<String> SqlGetStringArray(String sql) {
        ArrayList<String> IntArray = null;
        Cursor cursor = null;
        try {

            IntArray = new ArrayList<>();
            cursor = mDB.rawQuery(sql, null);
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                IntArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(TAG, "SqlGetStringArray: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
        return IntArray;
    }

    public HashMap<String, String> SqlGetStringMap(String sql) {

        HashMap<String, String> map = null;
        Cursor cursor = null;
        try {

            map = new HashMap<String, String>();
            cursor = mDB.rawQuery(sql, null);
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                map.put(cursor.getString(0), cursor.getString(1));
                isEof = cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(TAG, "SqlGetStringMap: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return map;
    }


    public ArrayList<HashMap<String, String>> SqlGetArrayMap(String sql) {

        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        Cursor cursor = null;
        try {

            cursor = mDB.rawQuery(sql, null);
            //Log.e(TAG, "SqlGetArrayMap: sql:"+sql );
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> map = null;
                map = new HashMap<String, String>();
                //Log.e(TAG, "SqlGetArrayMap: cursor.getColumnCount():"+cursor.getColumnCount() );
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    //Log.e(TAG, "SqlGetArrayMap: "+cursor.getColumnName(i)+","+ cursor.getString(i));
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                //Log.e(TAG, "SqlGetArrayMap: map:"+map.toString() );
                arrayList.add(map);
                isEof = cursor.moveToNext();

            }
            //Log.e(TAG, "SqlGetArrayMap: arrayList:"+arrayList.toString() );
        } catch (Exception e) {
            Log.e(TAG, "SqlGetArrayMap: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return arrayList;
    }

    @SuppressLint("SimpleDateFormat")
    public String getToday() {
        String value = "";
        Date today = new Date();
        value = (new SimpleDateFormat("yyyyMMdd").format(today));
        return value;
    }


    private final String data_group_by = "ordernum";
    private final String data_order_by = "orderid  and canceltime";
    //select * from etcamp_order where date = 20201231 group by ordernum  order by createtime  and canceltime

    //更新簡単記録データ
    public void updateSimpleDataArray(String date) {
        Cursor cursor = null;
        try {
            mSimpleDataArrayList.clear();
            String selection = "firstymd = " + date;
            cursor = mDB.query(
                    OrderListSql.orderTableName
                    , Frag01.pSimpleDataSqlNames
                    , selection
                    , null
                    , null
                    , null
                    , data_order_by);

            //pOrderListArrayListデータ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mOrderListMap = new HashMap<String, String>();
                for (int i = 0; i < Frag01.pSimpleDataSqlNames.length; i++) {

                    mOrderListMap.put(Frag01.pSimpleDataSqlNames[i], cursor.getString(i));
                }
                mSimpleDataArrayList.add(mOrderListMap);

                isEof = cursor.moveToNext();
            }
            //pOrderListArrayListデータ追加　end


        } catch (Exception e) {
            Log.e(TAG, "updateSimpleDataArray: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    //更新「サイト数」詳細記録OrderListデータ
    public void updateSiteDataArrayList(String date) {
        Cursor cursor = null;
        try {
            mSiteDataArrayList.clear();
            String selection = "firstymd = " + date;
            cursor = mDB.query(
                    OrderListSql.orderTableName
                    , OrderListSql.tableRowNames
                    , selection
                    , null
                    , data_group_by
                    , null
                    , data_order_by
            );

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mapOrderList = new HashMap<String, String>();
                for (int i = 0; i < OrderListSql.tableRowNames.length; i++) {

                    mapOrderList.put(OrderListSql.tableRowNames[i], cursor.getString(i));
                }
                mSiteDataArrayList.add(mapOrderList);
                isEof = cursor.moveToNext();
            }
            //データ追加　end

            //データ更新後、画面データ更新

            //データtest
            /*for (HashMap<String, String> mHashMap : mSiteDataArrayList) {

                Log.e(TAG, "getSiteDataMap:mHashMap.toString(): " + mHashMap.toString());
                for (String mSiteDataSqlName : OrderListSql.tableRowNames) {

                    String temp = mHashMap.get(mSiteDataSqlName);
                    //Log.d(TAG, "mSiteDialogDataArrayListデータテスト id:" + mSiteDataSqlName + " value:" + temp);
                }
            }
            Log.d(TAG, " mSiteDataArrayList.size():" + mSiteDataArrayList.size());*/
            //データテスト end


        } catch (Exception e) {
            Log.e(TAG, "getSiteDataMap: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    //更新「サイト　ROOM」詳細記録OrderListデータ
    public void updateSiteRoomDataArray(String date) {
        Cursor cursor = null;
        try {
            mSiteDataArrayList.clear();
            String selection = "firstymd = " + date;
            String[] mTableRowNames = {"siteid"};
            cursor = mDB.query(
                    OrderListSql.orderTableName
                    , mTableRowNames
                    , selection
                    , null
                    , data_group_by
                    , null
                    , data_order_by
            );

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mapOrderList = new HashMap<String, String>();
                for (int i = 0; i < OrderListSql.tableRowNames.length; i++) {

                    mapOrderList.put(OrderListSql.tableRowNames[i], cursor.getString(i));
                }
                mSiteDataArrayList.add(mapOrderList);
                isEof = cursor.moveToNext();
            }
            //データ追加　end

            //データ更新後、画面データ更新

            //データtest
            /*for (HashMap<String, String> mHashMap : mSiteDataArrayList) {

                Log.e(TAG, "getSiteDataMap:mHashMap.toString(): " + mHashMap.toString());
                for (String mSiteDataSqlName : OrderListSql.tableRowNames) {

                    String temp = mHashMap.get(mSiteDataSqlName);
                    //Log.d(TAG, "mSiteDialogDataArrayListデータテスト id:" + mSiteDataSqlName + " value:" + temp);
                }
            }
            Log.d(TAG, " mSiteDataArrayList.size():" + mSiteDataArrayList.size());*/
            //データテスト end


        } catch (Exception e) {
            Log.e(TAG, "getSiteDataMap: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
    }

    //更新「Dialog」詳細記録OrderListデータarray
    public HashMap<String, String> getDialogDataArray(String _ordernum, String _date) {
        HashMap<String, String> rMap = new HashMap<String, String>();
        Cursor cursor = null;
        try {
            String selection = " firstymd=" + _date + " and  ordernum = '" + _ordernum + "'";
            cursor = mDB.query(
                    OrderListSql.orderTableName
                    , Frag01DialogFragment.mSrcList
                    , selection
                    , null
                    , null
                    , null
                    , null
            );

            //データ追加
            boolean isEof = cursor.moveToFirst();
            for (int i = 0; i < Frag01DialogFragment.mSrcList.length; i++) {

                rMap.put(Frag01DialogFragment.mSrcList[i], cursor.getString(i));
            }
        } catch (Exception e) {
            Log.e(TAG, "getSiteDataMap: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    /**
     * 予約者、今回予約で予約ったサイト
     */
    public HashMap<String, String> getSiteDataMap(String _ordernum) {
        HashMap<String, String> rMap = new HashMap<String, String>();
        Cursor cursor = null;
        try {
            String sql = "select firstymd from etcamp_order where ordernum like '" + _ordernum + "' group by firstymd";
            cursor = mDB.rawQuery(sql, null);

            StringBuilder sqlDate = new StringBuilder("(");
            boolean e = cursor.moveToFirst();
            while (e) {
                String r = cursor.getString(0);
                if (cursor.isFirst()) {

                    sqlDate.append(" firstymd=").append(r);
                } else {
                    sqlDate.append(" or firstymd=").append(r);
                }
                e = cursor.moveToNext();
            }
            sqlDate.append(")");
            //Log.e(TAG, "getSiteDataMap: sqlDate:" + sqlDate);

            String selection = " ordernum = '" + _ordernum + "'" + " and " + sqlDate;
            cursor = mDB.query(
                    OrderListSql.orderTableName
                    , Frag01SiteFragment.mSrcArray
                    , selection
                    , null
                    , null
                    , null
                    , null
            );

            //データ追加
            boolean isEof = cursor.moveToFirst();
            for (int i = 0; i < Frag01SiteFragment.mSrcArray.length; i++) {

                rMap.put(Frag01SiteFragment.mSrcArray[i], cursor.getString(i));
            }
        } catch (Exception e) {
            Log.e(TAG, "getSiteDataMap: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    /**
     * 宿泊初日
     * "select min(firstymd) from etcamp_order where ordernum like '" + _ordernum + "' "
     */
    public String getSiteFirstDay(String _ordernum) {
        String rStr = "";
        Cursor cursor = null;
        try {
            String sql = "select min(firstymd) from etcamp_order where ordernum like '" + _ordernum + "' ";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            cursor.moveToFirst();
            rStr = cursor.getString(0);

        } catch (Exception e) {
            Log.e(TAG, "getSiteRoomsID: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rStr;
    }

    /**
     * 宿泊初日
     * "select min(firstymd) from etcamp_order where ordernum like '" + _ordernum + "' "
     */
    public ArrayList<String> getSiteDays(String _ordernum) {
        ArrayList<String> rArray = new ArrayList<>();

        Cursor cursor = null;
        try {
            String sql = "select firstymd from etcamp_order where ordernum='" + _ordernum + "'  group by firstymd order by firstymd";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getSiteRoomsID: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rArray;
    }


    /**
     * 「サイト数」詳細記録siteidデータ
     * "select siteid from etcamp_order where ordernum = '" + _ordernum + "' group by siteid"
     */
    public ArrayList<Integer> getSiteRoomsID(String _ordernum) {
        ArrayList<Integer> rArray = new ArrayList<>();

        Cursor cursor = null;
        try {
            String sql = "select siteid from etcamp_SiteList where ordernum = '" + _ordernum + "' group by siteid";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getInt(0));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getSiteRoomsID: rArray "+rArray.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getSiteRoomsID: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rArray;
    }

    public ArrayList<String> getSiteRoomsName(String _ordernum) {
        ArrayList<String> rArray = new ArrayList<>();

        Cursor cursor = null;
        try {
            String sql = "select sitename from etcamp_SiteList where ordernum = '" + _ordernum + "' group by siteid";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getSiteRoomsID: rArray "+rArray.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getSiteRoomsID: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rArray;
    }

    /**
     * 暂时为了能够看到房间号码，而临时做的数据对应。以后一定要修改
     * select siteid, sitename, sum(isac) from etcamp_SiteList where siteid>0 group by siteid order by siteid
     */
    public HashMap<Integer, String> getSqlRoomNames() {
        HashMap<Integer, String> rMap = new HashMap<>();

        Cursor cursor = null;
        try {

            String sql = "select siteid, sitename, sum(isac) from etcamp_SiteList where siteid>0 group by siteid order by siteid";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rMap.put(cursor.getInt(0), cursor.getString(1));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getRoomNames: rArray "+rMap.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getRoomNames: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    //Frag02で、毎月データ更新
    public HashMap<String, String> getRoomsOneMonth(int year, int month) {
        String sqlMonth = month < 10 ? "0" + month : "" + month;
        String yearMonth = year + "" + sqlMonth;
        HashMap<String, String> rMap = new HashMap<>();

        //select date,count(orderid) from etcamp_order
        // where  date > 20210100 and date<20210200 and canceltime like '' group by date
        Cursor cursor = null;
        try {

            /*String sql = "select firstymd,count(orderid) from etcamp_order where" +
                    " canceltime like '' and firstymd>" + yearMonth + "00 and firstymd<" + yearMonth + "32 group by firstymd";*/
            String sql = "select ymd,count(orderid) from etcamp_SiteList where  ymd>" + yearMonth + "00 and ymd<" + yearMonth + "32 group by ymd";
            cursor = mDB.rawQuery(sql, null);

            //Log.e(TAG, "getRoomsOneMonth: sql:"+sql );

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rMap.put(cursor.getString(0), cursor.getString(1));
                isEof = cursor.moveToNext();
            }
        } catch (Exception e) {
            Log.e(TAG, "getRoomsOneMonth: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    //更新「サイト数」詳細記録sitenameデータ
    public ArrayList<String> getSiteRoomNames(String _ordernum) {
        ArrayList<String> rArray = new ArrayList<>();
        Cursor cursor = null;
        try {

            /*String sql = "SELECT s.sitename\n" +
                    "        FROM etcamp_order o\n" +
                    "        left join etcamp_site s on (s.siteid= o.siteid) " +
                    " where o.ordernum = '" + _ordernum + "' group by o.siteid";*/

            String sql = "SELECT sitename FROM etcamp_SiteList where siteid>0 and ordernum = '"+_ordernum+"' group by siteid";


            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getSiteRoomsID: rArray "+rArray.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getSiteRoomsID: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rArray;
    }


    //更新「サイト room」詳細記録SiteRoomデータ
    public void updateSiteRoomDataArray() {
        Cursor cursor = null;
        //Log.e(TAG, "updateSiteRoomDataArray begin date:" + date);
        try {
            mSiteRoomArrayList.clear();
            cursor = mDB.query(
                    OrderListSql.SiteListTableName
                    , OrderListSql.SiteListRowNames
                    , ""
                    , null, null, null, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mapSiteList = new HashMap<String, String>();
                for (int i = 0; i < OrderListSql.SiteTableRowNames.length; i++) {

                    mapSiteList.put(OrderListSql.SiteTableRowNames[i], cursor.getString(i));
                }
                mSiteRoomArrayList.add(mapSiteList);
                isEof = cursor.moveToNext();
            }
            //データ追加　end

            //データ更新後、画面データ更新

            //データtest
            /*for (HashMap<String, String> mHashMap : mSiteRoomArrayList) {
                //Log.e(TAG, "updateSiteListDataArray:mHashMap.toString(): "+mHashMap.toString());
                for (String tableRowName : OrderListSql.SiteTableRowNames) {

                    String temp = mHashMap.get(tableRowName);
                }
            }*/
            //データテスト end

            //Log.e(TAG, "updateSiteRoomDataArray: "+mSiteRoomArrayList.size() );


        } catch (Exception e) {
            Log.e(TAG, "updateSiteRoomDataArray: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }


    public String getResultServerTime() throws JSONException {
        return getJsonObject().getJSONObject("result").getString("servertime");
    }

    public String getResultOrderList() throws JSONException {
        return getJsonObject().getJSONObject("result").getString("orderlist");
    }

}
