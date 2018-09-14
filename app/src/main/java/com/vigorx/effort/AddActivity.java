package com.vigorx.effort;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TimePicker;

import com.vigorx.effort.database.EffortOperations;
import com.vigorx.effort.entity.EffortInfo;
import com.vigorx.effort.entity.PunchesInfo;
import com.vigorx.effort.util.EffortAlarms;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity {
    public static final String TYPE_KEY = "type";
    public static final String EFFORT_KEY = "effort";
    public static final int TYPE_EDIT = 2;
    public static final int TYPE_ADD = 1;
    public static final String TAG = "AddActivity";

    private int mType;
    private EffortInfo mEffort;
    private EditText mTitle;
    private EditText mStartDate;
    private Switch mHaveAlarm;
    private TimePicker mAlarm;
    private String mTMPStartDate;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        assert toolbar != null;
        toolbar.setNavigationIcon(R.drawable.ic_bar_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mTitle = (EditText) findViewById(R.id.editTextTitle);

        mStartDate = (EditText) findViewById(R.id.editTextStartDate);
        mStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog;
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                dialog = new DatePickerDialog(AddActivity.this, dateSetListener, year, month, day);
                dialog.show();
            }
        });

        mHaveAlarm = (Switch) findViewById(R.id.switchRemind);

        mAlarm = (TimePicker) findViewById(R.id.timePickerRemind);

        Button okButton = (Button) findViewById(R.id.buttonOk);
        assert okButton != null;
        okButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // 从控件取值
                mEffort.setTitle(mTitle.getText().toString());
                mEffort.setStartDate(mStartDate.getText().toString());
                if (mHaveAlarm.isChecked()) {
                    mEffort.setHaveAlarm(1);
                } else {
                    mEffort.setHaveAlarm(0);
                }

                // 时间小于9，进行补零处理。
                String hour = mAlarm.getCurrentHour() > 9 ?
                        String.valueOf(mAlarm.getCurrentHour()) :
                        "0" + String.valueOf(mAlarm.getCurrentHour());
                String minute = mAlarm.getCurrentMinute() > 9 ?
                        String.valueOf(mAlarm.getCurrentMinute()) :
                        "0" + String.valueOf(mAlarm.getCurrentMinute());
                mEffort.setAlarm(hour + ":" + minute);

                // 写入数据库
                EffortOperations operator = EffortOperations.getInstance(AddActivity.this);
                operator.open();
                if (mType == 1) {
                    operator.addEffort(mEffort);
                } else {
                    operator.updateEffort(mEffort);
                }
                operator.close();

                // 设置闹钟
                EffortAlarms alarms = EffortAlarms.getInstance(AddActivity.this);
                if (mEffort.getHaveAlarm() == 1) {
                    alarms.add(mEffort.getId(), mEffort.getTitle(), mEffort.getAlarm());
                } else {
                    alarms.remove(mEffort.getId());
                }

                // 返回修正后的数据给详细画面。
                Intent intent = new Intent();
                intent.putExtra(EFFORT_KEY, mEffort);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        mType = getIntent().getIntExtra(TYPE_KEY, TYPE_ADD);
        if (mType == TYPE_EDIT) {       // 编辑目标
            mEffort = getIntent().getParcelableExtra(EFFORT_KEY);
            mTitle.setText(mEffort.getTitle());
            mStartDate.setText(mEffort.getStartDate());
            mHaveAlarm.setChecked((mEffort.getHaveAlarm() == 1));
            mAlarm.setCurrentHour(Integer.parseInt(mEffort.getAlarm().split(":")[0]));
            mAlarm.setCurrentMinute(Integer.parseInt(mEffort.getAlarm().split(":")[1]));
            setTitle(R.string.title_activity_edit);
        } else {                        // 追加目标
            mEffort = new EffortInfo();
            setTitle(R.string.title_activity_add);
        }
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String month = String.valueOf(monthOfYear + 1);
            if (monthOfYear < 9) {
                month = "0" + month;
            }

            String day = String.valueOf(dayOfMonth);
            if (dayOfMonth < 10) {
                day = "0" + day;
            }

            mTMPStartDate = year + "-" + month + "-" + day;

            if (mType == TYPE_EDIT) {
                AlertDialog dialog = new AlertDialog.Builder(AddActivity.this, R.style.customAlertDialog)
                        .setTitle(R.string.date_caption)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setMessage(R.string.date_confirm)
                        .setPositiveButton(R.string.OK, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mStartDate.setText(mTMPStartDate);
                                mEffort.setStartDate(mTMPStartDate);
                                PunchesInfo[] punches = mEffort.getPunches();
                                for (int i = 0; i < EffortInfo.EFFORT_SIZE; i ++) {
                                    punches[i].setComplete(0);
                                }
                            }
                        })
                        .setNegativeButton(R.string.CANCEL, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mStartDate.setText(mEffort.getStartDate());
                            }
                        })
                        .create();
                dialog.show();
            } else {
                mStartDate.setText(mTMPStartDate);
                mEffort.setStartDate(mTMPStartDate);
            }
        }
    };
}
