package com.vigorx.effort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vigorx.effort.EffortInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by songlei on 16/7/6.
 */
public class EffortOperations {
    private DataBaseWrapper mDBWrapper;
    private SQLiteDatabase mDatabase;
    private static EffortOperations mOperator = null;

    private String[] EFFORT_TABLE_COLUMNS = {DataBaseWrapper.EFFORT_ID,
            DataBaseWrapper.EFFORT_TITLE,
            DataBaseWrapper.EFFORT_DATE,
            DataBaseWrapper.EFFORT_HAVE_ALARM,
            DataBaseWrapper.EFFORT_ALARM};

    public static EffortOperations getInstance(Context context) {
        if (mOperator == null) {
            mOperator = new EffortOperations(context);
        }
        return mOperator;
    }

    private EffortOperations(Context context) {
        mDBWrapper = new DataBaseWrapper(context);
    }

    public void open() throws SQLException {
        mDatabase = mDBWrapper.getWritableDatabase();
    }

    public void close() {
        mDBWrapper.close();
    }

    public long addEffort(EffortInfo effort) {

        ContentValues values = new ContentValues();

        values.put(DataBaseWrapper.EFFORT_TITLE, effort.getTitle());
        values.put(DataBaseWrapper.EFFORT_DATE, effort.getStartDate());
        values.put(DataBaseWrapper.EFFORT_HAVE_ALARM, effort.getHaveAlarm());
        values.put(DataBaseWrapper.EFFORT_ALARM, effort.getAlarm());

        long effortId = mDatabase.insert(DataBaseWrapper.EFFORT_TABLE, null, values);

        return effortId;
    }

    public void deleteEffort(int effortId) {
        mDatabase.delete(DataBaseWrapper.EFFORT_TABLE, DataBaseWrapper.EFFORT_ID
                + " = " + effortId, null);
    }

    public int updateEffort(EffortInfo effort) {
        ContentValues values = new ContentValues();
        values.put(DataBaseWrapper.EFFORT_TITLE, effort.getTitle());
        values.put(DataBaseWrapper.EFFORT_DATE, effort.getStartDate());
        values.put(DataBaseWrapper.EFFORT_HAVE_ALARM, effort.getHaveAlarm());
        values.put(DataBaseWrapper.EFFORT_ALARM, effort.getAlarm());

        return mDatabase.update(DataBaseWrapper.EFFORT_TABLE, values,
                DataBaseWrapper.EFFORT_ID + " = " + effort.getId(), null);
    }

    public List<EffortInfo> getAllEffort() {
        List<EffortInfo> efforts = new ArrayList();

        Cursor cursor = mDatabase.query(DataBaseWrapper.EFFORT_TABLE,
                EFFORT_TABLE_COLUMNS, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EffortInfo effort = new EffortInfo();
            effort.setId(cursor.getInt(0));
            effort.setTitle(cursor.getString(1));
            effort.setStartDate(cursor.getString(2));
            effort.setHaveAlarm(cursor.getInt(3));
            effort.setAlarm(cursor.getString(4));
            efforts.add(effort);
            cursor.moveToNext();
        }

        cursor.close();
        return efforts;
    }

    public EffortInfo getEffortById(int id) {
        String selection = DataBaseWrapper.EFFORT_ID + " = " + id;
        Cursor cursor = mDatabase.query(DataBaseWrapper.EFFORT_TABLE,
                EFFORT_TABLE_COLUMNS, selection, null, null, null, null);
        cursor.moveToFirst();
        EffortInfo effort = new EffortInfo();
        effort.setId(cursor.getInt(0));
        effort.setTitle(cursor.getString(1));
        effort.setStartDate(cursor.getString(2));
        effort.setHaveAlarm(cursor.getInt(3));
        effort.setAlarm(cursor.getString(4));
        cursor.close();
        return effort;
    }

}
