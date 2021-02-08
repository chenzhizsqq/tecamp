package com.example.tecamp.sql;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class SiteRoomSql extends SQLiteOpenHelper {
    final static private int DB_VERSION = 1;
    public static final String SiteTableName = "etcamp_site";
    private static final String TAG = "SiteRoomSql";

    protected SQLiteDatabase db;


    public SiteRoomSql(Context context) {
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
    public SiteRoomSql(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
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



    @Override
    public void onCreate(SQLiteDatabase db) {
        // table create
        try {

            String sqlSiteStrContent = "";
            for (int i = 0; i < SiteTableRowNames.length; i++) {
                if(i>0){
                    sqlSiteStrContent += " , " + SiteTableRowNames[i] + " text ";
                }else{
                    sqlSiteStrContent +=  SiteTableRowNames[i] + " text ";
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
        String sqlStr="";
        db.execSQL(
                sqlStr
        );
    }
}