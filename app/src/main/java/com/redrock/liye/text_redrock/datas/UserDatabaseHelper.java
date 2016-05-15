package com.redrock.liye.text_redrock.datas;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by a on 2016/5/14.
 */
public class UserDatabaseHelper extends SQLiteOpenHelper {
    public static final String CREATE_USER = "create table User ("
            + "id integer primary key autoincrement,"
            + "account text,"
            + "password text)";
    public static final String CREATE_LIST = "create table List("
            + "id integer primary key autoincrement,"
            + "song text"
            + "singer text"
            + "pic_song text"
            + "download text"
            + "play text)";
    private Context mContext;
    public UserDatabaseHelper(Context context,String name,SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_LIST);
        Log.i("Redrock", "Create succeeded");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
