package com.vigorx.effort.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by songlei on 16/7/6.
 */
public class DataBaseWrapper extends SQLiteOpenHelper {

    // table t_effort
    public static final String EFFORT_TABLE = "t_effort";
    public static final String EFFORT_ID = "_id";
    public static final String EFFORT_TITLE = "_title";
    public static final String EFFORT_DATE = "_date";
    public static final String EFFORT_HAVE_ALARM = "_have_alarm";
    public static final String EFFORT_ALARM = "_alarm";

    // table t_punches
    public static final String PUNCHES_TABLE = "t_punches";
    public static final String PUNCHES_ID = "_id";
    public static final String PUNCHES_EFFORT_ID = "_effort_id";
    public static final String PUNCHES_OFFSET = "_offset";
    public static final String PUNCHES_COMPLETE = "_complete";

    private static final String DATABASE_NAME = "effort.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String EFFORT_CREATE = "create table " + EFFORT_TABLE
            + "(" + EFFORT_ID + " integer primary key autoincrement, "
            + EFFORT_TITLE + " text not null, "
            + EFFORT_DATE + " text, "
            + EFFORT_HAVE_ALARM + " integer, "
            + EFFORT_ALARM + " text);";

    // creation SQLite statement
    private static final String PUNCHES_CREATE = "create table " + PUNCHES_TABLE
            + "(" + PUNCHES_ID + " integer primary key autoincrement, "
            + PUNCHES_EFFORT_ID + " integer, "
            + PUNCHES_OFFSET + " integer, "
            + PUNCHES_COMPLETE + " integer);";

    public DataBaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EFFORT_CREATE);
        db.execSQL(PUNCHES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EFFORT_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + PUNCHES_TABLE);
        onCreate(db);
    }

}






