package com.vigorx.effort.util;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.vigorx.effort.R;

/**
 * Created by songlei on 16/7/28.
 */
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
//        AlertDialog dialog = new AlertDialog.Builder(context, R.style.customAlertDialog)
//                .setTitle(R.string.alarm_title)
//                .setIcon(R.mipmap.ic_launcher)
//                .setMessage(message)
//                .setPositiveButton(R.string.delete_ok, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                })
//                .create();
//        dialog.show();
    }
}
