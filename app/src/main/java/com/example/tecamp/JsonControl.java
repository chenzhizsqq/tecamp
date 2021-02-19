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
                Log.e(TAG, "LoginInit: getResult:" + getResult);
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
    public static boolean pJsonGetDataNext = true;
    public static String mGetLastJsonObjGetDate = "";

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
                mJsonObject = new JSONObject(getResult);
                Log.e(TAG, "MakeJsonGetData: getResult:" + getResult);
                status = getJsonObject().getString("status");
                //if (getJsonObject().getString("status").equals("0")) {
                if (status.equals("0")) {
                    String getJsonObjGetDate = getJsonObject().toString();
                    //Log.e(TAG, "GetData: mGetJsonObjGetDate:" + getJsonObjGetDate);
                    DateManager mDateManager = new DateManager();
                    //Log.e(TAG, "getYMDHMS: " + mDateManager.getYMDHMS());
                    Config.jsonGetLastTime = mDateManager.getYMDHMS();
                    if (!mGetLastJsonObjGetDate.equals(getJsonObjGetDate)) {

                        mGetLastJsonObjGetDate = getJsonObjGetDate;
                        pJsonGetDataNext = true;
                    } else {

                        pJsonGetDataNext = false;
                    }
                } else {
                    pJsonGetDataNext = false;
                }
            } else {
                throw new IOException("Unexpected code " + response);
            }
        } catch (IOException | JSONException e) {
            Log.e(TAG, "MakeJsonGetData: ", e);
        }

        return status;
    }

    //orderlistのjson
    public int getJSONArrayOrderListLength() throws JSONException {
        return getJsonObject().getJSONObject("result").getJSONArray("orderlist").length();
    }

    public String getJSONArrayOrderList(int index, String name) throws JSONException {

        return getJsonObject().getJSONObject("result").getJSONArray("orderlist").getJSONObject(index).getString(name);
    }


    //sitelistのjson
    public int getJSONArraySiteListLength() throws JSONException {
        return getJsonObject().getJSONObject("result").getJSONArray("sitelist").length();
    }

    public String getJSONArraySiteList(int index, String name) throws JSONException {

        return getJsonObject().getJSONObject("result").getJSONArray("sitelist").getJSONObject(index).getString(name);
    }


    //EtCampGetData でOrderListのjsonデータを貰います。
    public String getOrderListJsonValue(int index, String name) throws JSONException {
        String rStrValue = "";
        try {
            rStrValue = getJSONArrayOrderList(index, name);
        } catch (Exception e) {
            Log.e(TAG, "getOrderListJsonValue: ", e);
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
            "date",
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
                case "date":
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
            //Log.e(TAG, "Json2Sql: getJSONArrayOrderListLength():"+getJSONArrayOrderListLength() );
            if (getJSONArrayOrderListLength() > 0) {
            } else {
                return;
            }
            //OrderListのjson、SQLデータを入ります
            for (int j = 0; j < getJSONArrayOrderListLength(); j++) {

                ContentValues values = new ContentValues();
                for (int i = 0; i < OrderListSql.tableRowNames.length; i++) {
                    values.put(OrderListSql.tableRowNames[i], this.getOrderListJsonValue(j, OrderListSql.tableRowNames[i]));
                }

                String sWhere = "orderid =" + this.getOrderListJsonValue(j, "orderid");
                String testSql = "select orderid from " + OrderListSql.tableName + " where " + sWhere;
                int count = getSqlCount(testSql);
                if (count > 0) {

                    mDB.update(OrderListSql.tableName, values, sWhere, null);
                } else {

                    mDB.insert(OrderListSql.tableName, null, values);
                }


                testSql = "select orderid from " + OrderListSql.tableNameTemp + " where " + sWhere;
                count = getSqlCount(testSql);
                if (count > 0) {

                    mDB.update(OrderListSql.tableNameTemp, values, sWhere, null);
                } else {

                    mDB.insert(OrderListSql.tableNameTemp, null, values);
                }
                values.clear();
            }


        } catch (Exception e) {
            Log.e(TAG, "Json2Sql: ", e);
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

                String sWhere = "siteid =" + this.getSiteListJsonValue(j, OrderListSql.SiteTableRowNames[0]);
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
            Log.e(TAG, "Json2Sql: ", e);
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
            String selection = "date = " + date;
            cursor = mDB.query(
                    OrderListSql.tableName
                    , Frag01.pSimpleDataSqlNames
                    , selection
                    , null
                    , data_group_by
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
    public void updateSiteDataArray(String date) {
        Cursor cursor = null;
        try {
            mSiteDataArrayList.clear();
            String selection = "date = " + date;
            cursor = mDB.query(
                    OrderListSql.tableName
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

                Log.e(TAG, "updateSiteDataArray:mHashMap.toString(): " + mHashMap.toString());
                for (String mSiteDataSqlName : OrderListSql.tableRowNames) {

                    String temp = mHashMap.get(mSiteDataSqlName);
                    //Log.d(TAG, "mSiteDialogDataArrayListデータテスト id:" + mSiteDataSqlName + " value:" + temp);
                }
            }
            Log.d(TAG, " mSiteDataArrayList.size():" + mSiteDataArrayList.size());*/
            //データテスト end


        } catch (Exception e) {
            Log.e(TAG, "updateSiteDataArray: ", e);
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
            String selection = "date = " + date;
            String[] mTableRowNames = {"siteid"};
            cursor = mDB.query(
                    OrderListSql.tableName
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

                Log.e(TAG, "updateSiteDataArray:mHashMap.toString(): " + mHashMap.toString());
                for (String mSiteDataSqlName : OrderListSql.tableRowNames) {

                    String temp = mHashMap.get(mSiteDataSqlName);
                    //Log.d(TAG, "mSiteDialogDataArrayListデータテスト id:" + mSiteDataSqlName + " value:" + temp);
                }
            }
            Log.d(TAG, " mSiteDataArrayList.size():" + mSiteDataArrayList.size());*/
            //データテスト end


        } catch (Exception e) {
            Log.e(TAG, "updateSiteDataArray: ", e);
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
            String selection = " date=" + _date + " and  ordernum = '" + _ordernum + "'";
            cursor = mDB.query(
                    OrderListSql.tableName
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
            Log.e(TAG, "updateSiteDataArray: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    /**
     * 予約者、今回予約で予約ったサイト
     */
    public HashMap<String, String> updateSiteDataArray(String _ordernum, String _date) {
        HashMap<String, String> rMap = new HashMap<String, String>();
        Cursor cursor = null;
        try {
            String sql = "select date from etcamp_order where ordernum like '" + _ordernum + "' group by date";
            cursor = mDB.rawQuery(sql, null);

            StringBuilder sqlDate = new StringBuilder("(");
            boolean e = cursor.moveToFirst();
            while (e) {
                String r = cursor.getString(0);
                if (cursor.isFirst()) {

                    sqlDate.append(" date=").append(r);
                } else {
                    sqlDate.append(" or date=").append(r);
                }
                e = cursor.moveToNext();
            }
            sqlDate.append(")");
            //Log.e(TAG, "updateSiteDataArray: sqlDate:" + sqlDate);

            String selection = " ordernum = '" + _ordernum + "'" + " and " + sqlDate;
            cursor = mDB.query(
                    OrderListSql.tableName
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
            Log.e(TAG, "updateSiteDataArray: ", e);
        } finally {

            if (cursor != null)
                cursor.close();
        }
        return rMap;
    }

    /**
     * 宿泊初日
     * "select min(date) from etcamp_order where ordernum like '" + _ordernum + "' "
     */
    public String getSiteFirstDay(String _ordernum) {
        String rStr = "";
        Cursor cursor = null;
        try {
            String sql = "select min(date) from etcamp_order where ordernum like '" + _ordernum + "' ";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            cursor.moveToFirst();
            rStr = cursor.getString(0);

        } catch (Exception e) {
            Log.e(TAG, "getSiteRooms: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rStr;
    }

    /**
     * 宿泊初日
     * "select min(date) from etcamp_order where ordernum like '" + _ordernum + "' "
     */
    public ArrayList<String> getSiteDays(String _ordernum) {
        ArrayList<String> rArray = new ArrayList<>();

        Cursor cursor = null;
        try {
            String sql = "select date from etcamp_order where ordernum='" + _ordernum + "'  group by date order by date";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }

        } catch (Exception e) {
            Log.e(TAG, "getSiteRooms: ", e);
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
    public ArrayList<Integer> getSiteRooms(String _ordernum) {
        ArrayList<Integer> rArray = new ArrayList<>();

        Cursor cursor = null;
        try {
            String sql = "select siteid from etcamp_order where ordernum = '" + _ordernum + "' group by siteid";
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getInt(0));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getSiteRooms: rArray "+rArray.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getSiteRooms: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return rArray;
    }

    /**
     * select siteid, sitename from etcamp_site order by siteid
     */
    public HashMap<Integer, String> getSqlRoomNames() {
        HashMap<Integer, String> rMap = new HashMap<>();

        Cursor cursor = null;
        try {

            String sql = "select siteid, sitename from etcamp_site order by siteid";
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

            String sql = "select date,count(orderid) from etcamp_order where" +
                    " canceltime like '' and date>" + yearMonth + "00 and date<" + yearMonth + "32 group by date";
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

            String sql = "SELECT s.sitename\n" +
                    "        FROM etcamp_order o\n" +
                    "        left join etcamp_site s on (s.siteid= o.siteid) " +
                    " where o.ordernum = '" + _ordernum + "' group by o.siteid";

            cursor = mDB.rawQuery(sql, null);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                rArray.add(cursor.getString(0));
                isEof = cursor.moveToNext();
            }
            //Log.e(TAG, "getSiteRooms: rArray "+rArray.toString() );
        } catch (Exception e) {
            Log.e(TAG, "getSiteRooms: ", e);
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
                    OrderListSql.SiteTableName
                    , OrderListSql.SiteTableRowNames
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

    public void testSql(String _date) {
        Cursor cursor = null;
        try {

            String sql = "SELECT o.*,s.sitename as name ,t.date as firstdate \n" +
                    "  FROM etcamp_order o\n" +
                    " LEFT JOIN etcamp_site s on ( s.siteid= o.siteid )" +
                    " LEFT JOIN etcamp_order_temp t on ( t.ordernum= o.ordernum ) " + " where o.date = " + _date;
            cursor = mDB.rawQuery(sql, null);

            //データ追加
            ArrayList<HashMap<String, String>> mTestArrayList = new ArrayList<>();
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mapOrderList = new HashMap<String, String>();
                String[] name = cursor.getColumnNames();
                for (int i = 0; i < cursor.getColumnCount(); i++) {

                    mapOrderList.put(name[i], cursor.getString(i));
                }

                mTestArrayList.add(mapOrderList);

                isEof = cursor.moveToNext();
            }
            //データ追加　end

            //データ更新後、画面データ更新

            //データtest
            /*Log.e(TAG, " mTestArrayList.size():" + mTestArrayList.size());
            for (HashMap<String, String> mHashMap : mTestArrayList) {
                Log.e(TAG, "mTestArrayList:mHashMap.toString(): " + mHashMap.toString());
            }*/
            //データテスト end
        } catch (Exception e) {
            Log.e(TAG, "testSql e: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

    //更新「テスト」テスト記録データ
    public void updateTestArray(String date) {
        Cursor cursor = null;
        try {
            mTestDataArrayList.clear();
            String selection = "date = " + date;
            cursor = mDB.query(
                    OrderListSql.tableName
                    , OrderListSql.tableRowNames
                    , selection
                    , null, null, null, data_order_by);

            //データ追加
            boolean isEof = cursor.moveToFirst();
            while (isEof) {
                HashMap<String, String> mapOrderList = new HashMap<String, String>();
                for (int i = 0; i < OrderListSql.tableRowNames.length; i++) {

                    mapOrderList.put(OrderListSql.tableRowNames[i], cursor.getString(i));
                }
                mTestDataArrayList.add(mapOrderList);
                isEof = cursor.moveToNext();
            }
            //データ追加　end

            //データ更新後、画面データ更新

            //データtest
            //Log.e(TAG, " mTestDataArrayList.size():" + mTestDataArrayList.size());
            for (HashMap<String, String> mHashMap : mTestDataArrayList) {

                //Log.e(TAG, "updateTestArray:mHashMap.toString(): " + mHashMap.toString());
                for (String rowName : OrderListSql.tableRowNames) {

                    String temp = mHashMap.get(rowName);
                    //Log.d(TAG, "updateTestArray id:" + rowName + " value:" + temp);
                }
            }
            //データテスト end


        } catch (Exception e) {
            Log.e(TAG, "updateTestArray: ", e);
        } finally {
            if (cursor != null)
                cursor.close();
        }
    }

}
