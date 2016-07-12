package com.vigorx.effort.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.vigorx.effort.entity.EffortInfo;
import com.vigorx.effort.entity.PunchesInfo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by songlei on 16/7/6.
 */
public class EffortOperations {
    private DataBaseWrapper mDBWrapper;
    private SQLiteDatabase mDatabase;
    private static EffortOperations mOperator = null;

    // table t_effort
    private String[] EFFORT_TABLE_COLUMNS = {DataBaseWrapper.EFFORT_ID,
            DataBaseWrapper.EFFORT_TITLE,
            DataBaseWrapper.EFFORT_DATE,
            DataBaseWrapper.EFFORT_HAVE_ALARM,
            DataBaseWrapper.EFFORT_ALARM};

    // table t_punches
    private String[] PUNCHES_TABLE_COLUMNS = {DataBaseWrapper.PUNCHES_ID,
            DataBaseWrapper.PUNCHES_EFFORT_ID,
            DataBaseWrapper.PUNCHES_OFFSET,
            DataBaseWrapper.PUNCHES_COMPLETE};

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
        addPunchesByEffort((int) effortId);

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
            effort.setPunches(getPunchesByEffort(effort.getId()));
            efforts.add(effort);
            cursor.moveToNext();
        }

        cursor.close();
        return efforts;
    }

    public List<EffortInfo> getTodayEffort() {
        ArrayList<EffortInfo> efforts = new ArrayList();

        String selection = "julianday('now')-julianday(" + DataBaseWrapper.EFFORT_DATE + ")"
                + " > " + EffortInfo.EFFORT_SIZE;
        Cursor cursor = mDatabase.query(DataBaseWrapper.EFFORT_TABLE,
                EFFORT_TABLE_COLUMNS, selection, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            EffortInfo effort = new EffortInfo();
            effort.setId(cursor.getInt(0));
            effort.setTitle(cursor.getString(1));
            effort.setStartDate(cursor.getString(2));
            effort.setHaveAlarm(cursor.getInt(3));
            effort.setAlarm(cursor.getString(4));
            effort.setPunches(getPunchesByEffort(effort.getId()));
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

    private void addPunchesByEffort(int effortId) {

        for (int i = 0; i < EffortInfo.EFFORT_SIZE; i++) {
            ContentValues values = new ContentValues();

            values.put(DataBaseWrapper.PUNCHES_EFFORT_ID, effortId);
            values.put(DataBaseWrapper.PUNCHES_OFFSET, i);
            values.put(DataBaseWrapper.PUNCHES_COMPLETE, 0);
            mDatabase.insert(DataBaseWrapper.PUNCHES_TABLE, null, values);
        }
    }

    public void updatePunches(PunchesInfo[] punches) {
        for (int i = 0; i < EffortInfo.EFFORT_SIZE; i++) {
            ContentValues values = new ContentValues();
            values.put(DataBaseWrapper.PUNCHES_COMPLETE, punches[i].getComplete());

            int update = mDatabase.update(DataBaseWrapper.PUNCHES_TABLE, values,
                    DataBaseWrapper.PUNCHES_ID + " = " + punches[i].getId(), null);
        }
    }

    public void deletePunchesByEffort(int effortId) {
        mDatabase.delete(DataBaseWrapper.PUNCHES_TABLE, DataBaseWrapper.PUNCHES_EFFORT_ID
                + " = " + effortId, null);
    }

    private PunchesInfo[] getPunchesByEffort(int effortId) {
        String selection = DataBaseWrapper.PUNCHES_EFFORT_ID + " = " + effortId;
        Cursor cursor = mDatabase.query(DataBaseWrapper.PUNCHES_TABLE,
                PUNCHES_TABLE_COLUMNS, selection, null, null, null, DataBaseWrapper.PUNCHES_ID + " asc");
        cursor.moveToFirst();
        PunchesInfo[] infos = new PunchesInfo[EffortInfo.EFFORT_SIZE];

        int index = 0;
        while (!cursor.isAfterLast()) {
            PunchesInfo info = new PunchesInfo();
            info.setId(cursor.getInt(0));
            info.setEffortId(cursor.getInt(1));
            info.setOffset(cursor.getInt(2));
            info.setComplete(cursor.getInt(3));
            infos[index] = info;
            index++;
            cursor.moveToNext();
        }

        cursor.close();
        return infos;
    }

}
