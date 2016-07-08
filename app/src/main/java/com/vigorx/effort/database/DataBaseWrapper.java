package com.vigorx.effort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.format.Time;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by songlei on 16/7/6.
 */
public class DataBaseWrapper extends SQLiteOpenHelper {

    public static final String EFFORT_TABLE = "t_effort";
    public static final String EFFORT_ID = "_id";
    public static final String EFFORT_TITLE = "_title";
    public static final String EFFORT_DATE = "_date";
    public static final String EFFORT_HAVE_ALARM = "_have_alarm";
    public static final String EFFORT_ALARM = "_alarm";

    private static final String DATABASE_NAME = "effort.db";
    private static final int DATABASE_VERSION = 1;

    // creation SQLite statement
    private static final String DATABASE_CREATE = "create table " + EFFORT_TABLE
            + "(" + EFFORT_ID + " integer primary key autoincrement, "
            + EFFORT_TITLE + " text not null, "
            + EFFORT_DATE + " text, "
            + EFFORT_HAVE_ALARM + " integer, "
            + EFFORT_ALARM + " text);";

    public DataBaseWrapper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
        ContentValues cv = new ContentValues(4);
        cv.put(EFFORT_TITLE, "每日练习书法15分钟。");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        cv.put(EFFORT_DATE, dateFormat.format(calendar.getTime()));
        cv.put(EFFORT_HAVE_ALARM, 1);
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM");
        cv.put(EFFORT_ALARM, timeFormat.format(calendar.getTime()));
        db.insert(EFFORT_TABLE, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // you should do some logging in here
        // ..

        db.execSQL("DROP TABLE IF EXISTS " + EFFORT_TABLE);
        onCreate(db);
    }

}






