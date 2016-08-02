package com.vigorx.effort;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import com.vigorx.effort.util.EffortAlarms;

public class AlarmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        final MediaPlayer mediaPlayer = MediaPlayer.create(AlarmActivity.this,
                RingtoneManager.getActualDefaultRingtoneUri(AlarmActivity.this, RingtoneManager.TYPE_ALARM));
        mediaPlayer.setLooping(true);
        mediaPlayer.start();

        Intent intent = getIntent();
        String message = intent.getStringExtra(EffortAlarms.ALARM_MESSAGE);
        AlertDialog dialog = new AlertDialog.Builder(AlarmActivity.this, R.style.customAlertDialog)
                .setTitle(R.string.alarm_title)
                .setIcon(android.R.drawable.ic_lock_idle_alarm)
                .setMessage(message)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mediaPlayer.stop();
                        finish();
                    }
                })
                .create();
        // 防止点击在对话框外部时，关闭对话框。
        dialog.setCancelable(false);
        dialog.show();
    }
}
