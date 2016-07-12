package com.vigorx.effort;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.vigorx.effort.calendar.CalendarAdapter;
import com.vigorx.effort.calendar.CalendarView;
import com.vigorx.effort.calendar.ClickDataListener;
import com.vigorx.effort.database.EffortOperations;
import com.vigorx.effort.entity.EffortInfo;
import com.vigorx.effort.entity.PunchesInfo;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity implements ClickDataListener{

    private static final String TAG = "DetailActivity";
    public static final String EFFORT_KEY = "effort";

    private PieChart mChart;
    private EffortInfo mEffort;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
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

        mEffort = getIntent().getParcelableExtra(EFFORT_KEY);

        calendarView = (CalendarView) findViewById(R.id.calendar);
        calendarView.initView(mEffort.getStartDate(), mEffort.getPunches());
        calendarView.setClickDataListener(this);

        mChart = (PieChart) findViewById(R.id.detail_chart);
        showPieChart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mChart.invalidate();
    }

    @Override
    protected void onPause() {
        super.onPause();

        EffortOperations operations = EffortOperations.getInstance(this);
        operations.open();
        operations.updatePunches(mEffort.getPunches());
        operations.close();
    }

    private void showPieChart() {
        float fontSize = 13f;
        // 半径
        mChart.setHoleRadius(50f);
        // 半透明圈
        mChart.setTransparentCircleRadius(53f);
        // 饼图中间的文字
        if (mEffort.getHaveAlarm() == 1) {
            mChart.setCenterText(mEffort.getAlarm());
        }
        else {
            mChart.setCenterText(getResources().getString(R.string.alarm_off));
        }
        mChart.setCenterTextSize(fontSize);
        // 可以手动旋转
        mChart.setRotationEnabled(true);
        // 显示成百分比
        mChart.setUsePercentValues(true);
        // 孔洞设置
        mChart.setDrawHoleEnabled(true);
        // 显示文本在饼块
        mChart.setDrawEntryLabels(false);
        mChart.setEntryLabelTextSize(fontSize);

        // 描述文字
        String description = getResources().getString(R.string.startDate) + mEffort.getStartDate();
        mChart.setDescription(description);
        mChart.setDescriptionTextSize(fontSize);

        mChart.setData(getPieData());

        // 动画设置
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        // 设置图例
        Legend l = mChart.getLegend();
        l.setEnabled(true);
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setXEntrySpace(17f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
        l.setTextSize(fontSize);
    }

    private PieData getPieData() {

        // 饼块的数据
        ArrayList<PieEntry> yValues = new ArrayList<>();
        yValues.add(new PieEntry(getCompleteNumber(), getResources().getString(R.string.complete)));
        yValues.add(new PieEntry(getProgressNumber(), getResources().getString(R.string.progress)));

        PieDataSet pieDataSet = new PieDataSet(yValues, mEffort.getTitle());

        // 饼块颜色
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorCompletePie));
        colors.add(getResources().getColor(R.color.colorProgressPie));
        pieDataSet.setColors(colors);

        // 选中态多出的长度
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 220f);
        pieDataSet.setSelectionShift(px);

        // 设置值的字体大小
        pieDataSet.setValueTextSize(12f);

        PieData data = new PieData(pieDataSet);

        // 设置格式百分比
        data.setValueFormatter(new PercentFormatter());
        return data;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_share:
                Toast.makeText(this, R.string.msg_work, Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_edit:
                Intent intent = new Intent(DetailActivity.this, AddActivity.class);
                intent.putExtra(AddActivity.TYPE_KEY, AddActivity.TYPE_EDIT);
                intent.putExtra(AddActivity.EFFORT_KEY, mEffort);
                startActivity(intent);
                break;
            case R.id.action_delete:
                AlertDialog dialog = new AlertDialog.Builder(DetailActivity.this)
                        .setTitle(R.string.delete_caption)
                        .setIcon(android.R.drawable.ic_menu_delete)
                        .setMessage(R.string.delete_confirm)
                        .setPositiveButton(R.string.delete_ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EffortOperations operator = EffortOperations.getInstance(DetailActivity.this);
                                operator.open();
                                operator.deleteEffort(mEffort.getId());
                                operator.close();
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.delete_cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create();
                dialog.show();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private int getCompleteNumber() {
        int number = 0;
        PunchesInfo[] infos = mEffort.getPunches();
        for (int i = 0; i < EffortInfo.EFFORT_SIZE; i++) {
            if (1 == infos[i].getComplete()) {
                number++;
            }
        }
        return number;
    }

    private int getProgressNumber() {
        return EffortInfo.EFFORT_SIZE - getCompleteNumber();
    }

    @Override
    public void clickData() {
        mChart.setData(getPieData());
        mChart.invalidate();
    }
}
