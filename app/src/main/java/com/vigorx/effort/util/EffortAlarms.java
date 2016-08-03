package com.vigorx.effort.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;

import com.vigorx.effort.AlarmActivity;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by songlei on 16/7/28.
 */
public class EffortAlarms {
    public static final String ALARM_MESSAGE = "message";

    private static EffortAlarms mInstance;
    private Context mContext;
    private AlarmManager mAlarmManager;
    private Map mIntentMap = new HashMap();

    private EffortAlarms(Context context) {
        mContext = context.getApplicationContext();
        mAlarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
    }

    public static EffortAlarms getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new EffortAlarms(context);
        }
        return mInstance;
    }

    public void add(int id, String message, String time) {
        int hour = Integer.parseInt(time.split(":")[0]);
        int minute = Integer.parseInt(time.split(":")[1]);
        Intent intent = new Intent(mContext, AlarmActivity.class);
        intent.putExtra(ALARM_MESSAGE, message);
        PendingIntent pi = PendingIntent.getActivity(mContext, id, intent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        mAlarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 24 * 60 * 60 * 1000, pi);
        mIntentMap.put(String.valueOf(id), pi);
    }

    public void remove(int id) {
        String mapId = String.valueOf(id);
        if (mIntentMap.containsKey(mapId)) {
            PendingIntent intent = (PendingIntent) mIntentMap.get(id);
            mAlarmManager.cancel(intent);
            mIntentMap.remove(mapId);
        }
    }

    public void clear() {
        for (Object object : mIntentMap.keySet()){
            PendingIntent intent = (PendingIntent) mIntentMap.get(object);
            mAlarmManager.cancel(intent);
        }
        mIntentMap.clear();
    }
}

