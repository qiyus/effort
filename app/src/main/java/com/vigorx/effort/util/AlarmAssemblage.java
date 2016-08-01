package com.vigorx.effort.util;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

/**
 * Created by songlei on 16/7/28.
 */
public class AlarmAssemblage {

    private static class AlarmHolder {
        private static AlarmAssemblage mInstance = new AlarmAssemblage();
    }

    private Context mContext;

    public static AlarmAssemblage getInstance(Context context) {
        AlarmHolder.mInstance.mContext = context;
        return AlarmHolder.mInstance;
    }

    public void start() {
        Intent intent = new Intent("EFFORT_ALARM");
        intent.putExtra("message", "test");
        PendingIntent pi = PendingIntent.getBroadcast(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager manager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), 70000, pi);
    }
}

