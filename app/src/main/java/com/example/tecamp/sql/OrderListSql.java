package com.example.tecamp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class OrderListSql extends SQLiteOpenHelper {
    final static private int DB_VERSION = 1;
    public static final String tableName = "etcamp_order";
    public static final String tableNameTemp = "etcamp_order_temp";
    public static final String SiteTableName = "etcamp_site";
    private static final String TAG = "OrderListSql";

    protected SQLiteDatabase db;


    public OrderListSql(Context context) {
        super(context, null, null, DB_VERSION);
    }

    /**
     * Create a helper object to create, open, and/or manage a database.
     * This method always returns very quickly.  The database is not actually
     * created or opened until one of {@link #getWritableDatabase} or
     * {@link #getReadableDatabase} is called.
     *
     * @param context to use for locating paths to the the database
     * @param name    of the database file, or null for an in-memory database
     * @param factory to use for creating cursor objects, or null for the default
     * @param version number of the database (starting at 1); if the database is older,
     *                {@link #onUpgrade} will be used to upgrade the database; if the database is
     *                newer, {@link #onDowngrade} will be used to downgrade the database
     */
    public OrderListSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public final static String[] SiteTableRowNames = {
            "siteid",
            "sitename",
            "area",
            "acflg",
            "updatetime",
            "updateuser",

    };

    public final static String[] tableRowNames = {
            "orderid",
            "ordernum",
            "date",
            "userid",
            "username",
            "username_kana",
            "username2",
            "username_kana2",
            "address",
            "tel_1",
            "tel_2",
            "tel_3",
            "mail",
            "count_adult",
            "count_child",
            "siteid",
            "site_count",
            "ac_count",
            "days",
            "fee",
            "ac_check",
            "camper",
            "memo",
            "way",
            "orderflag",
            "admemo",
            "canceltime",
            "ipaddress",
            "createtime",
            "createuser",
            "updatetime",
            "updateuser"
    };



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE  IF EXISTS "+tableName);
        db.execSQL("DROP TABLE  IF EXISTS "+tableNameTemp);
        db.execSQL("DROP TABLE  IF EXISTS "+SiteTableName);

        //Log.e(TAG, "onCreate: begin" );
        // table create
        StringBuilder sqlStrContent = new StringBuilder();
        for (int i = 0; i < tableRowNames.length; i++) {
            if(i>0){
                sqlStrContent.append(" , ").append(tableRowNames[i]).append(" text ");
            }else{
                sqlStrContent.append(tableRowNames[i]).append(" integer primary key ");
            }
        }
        String sqlStr = "create table " + tableName + "(" +
                sqlStrContent +
                ");";
        Log.d(TAG, "onCreate- sqlStr:" + sqlStr);
        db.execSQL(
                sqlStr
        );

        sqlStr = "create table " + tableNameTemp + "(" +
                sqlStrContent +
                ");";
        Log.d(TAG, "onCreate- sqlStr:" + sqlStr);
        db.execSQL(
                sqlStr
        );
        onSiteCreate(db);
    }


    public void onSiteCreate(SQLiteDatabase db) {
        // table create
        try {

            StringBuilder sqlSiteStrContent = new StringBuilder();
            for (int i = 0; i < SiteTableRowNames.length; i++) {
                if(i>0){
                    sqlSiteStrContent.append(" , ").append(SiteTableRowNames[i]).append(" text ");
                }else{
                    sqlSiteStrContent.append(SiteTableRowNames[i]).append(" integer primary key");
                }
            }
            String sqlStr = "create table " + SiteTableName + "(" +
                    sqlSiteStrContent +
                    ");";
            Log.d(TAG, "onCreate- SiteRoomSql:" + sqlStr);
            db.execSQL(
                    sqlStr
            );
        }catch (Exception e){
            Log.e(TAG, "onCreate: ",e );
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // データベースの変更が生じた場合は、ここに処理を記述する。
        //Log.e(TAG, "onUpgrade: begin" );
        if(oldVersion!=newVersion)
        {
            onCreate(db);
        }
    }
}