package com.vigorx.effort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.vigorx.effort.database.EffortOperations;
import com.vigorx.effort.entity.EffortInfo;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ReportActivity extends AppCompatActivity implements View.OnClickListener {
    BarChart mChart;
    ImageButton mCurrentButton;
    ListView mListView;
    List<EffortInfo> mYearEffort;
    float[] mChartData = new float[12];
    int mCurrentYear;
    int mCurrentMonth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
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

        mChart = (BarChart) findViewById(R.id.report_chart);
        mListView = (ListView) findViewById(R.id.report_list);
        mListView.setEmptyView(findViewById(R.id.emptyView));
        Calendar calendar = Calendar.getInstance();
        mCurrentYear = calendar.get(Calendar.YEAR);
        mCurrentMonth = calendar.get(Calendar.MONTH) + 1;
        setCurrentMonth();
    }

    private void setCurrentMonth() {
        switch (mCurrentMonth) {
            case 1:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton1);
                mCurrentButton.setBackgroundResource(R.drawable.month1_press);
                break;
            case 2:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton2);
                mCurrentButton.setBackgroundResource(R.drawable.month2_press);
                break;
            case 3:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton3);
                mCurrentButton.setBackgroundResource(R.drawable.month3_press);
                break;
            case 4:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton4);
                mCurrentButton.setBackgroundResource(R.drawable.month4_press);
                break;
            case 5:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton5);
                mCurrentButton.setBackgroundResource(R.drawable.month5_press);
                break;
            case 6:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton6);
                mCurrentButton.setBackgroundResource(R.drawable.month6_press);
                break;
            case 7:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton7);
                mCurrentButton.setBackgroundResource(R.drawable.month7_press);
                break;
            case 8:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton8);
                mCurrentButton.setBackgroundResource(R.drawable.month8_press);
                break;
            case 9:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton9);
                mCurrentButton.setBackgroundResource(R.drawable.month9_press);
                break;
            case 10:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton10);
                mCurrentButton.setBackgroundResource(R.drawable.month10_press);
                break;
            case 11:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton11);
                mCurrentButton.setBackgroundResource(R.drawable.month11_press);
                break;
            case 12:
                mCurrentButton = (ImageButton) findViewById(R.id.imageButton12);
                mCurrentButton.setBackgroundResource(R.drawable.month12_press);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        EffortOperations operations = EffortOperations.getInstance(ReportActivity.this);
        operations.open();
        mYearEffort = operations.getYearEffort(String.valueOf(mCurrentYear));
        operations.close();
        updateChartData();
        showChart();
        mListView.setAdapter(new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(mCurrentMonth)));
    }

    private void updateChartData() {
        for (EffortInfo info : mYearEffort) {
            int month = Integer.parseInt(info.getStartDate().split("-")[1]);
            mChartData[month - 1]++;
        }
    }

    private List<EffortInfo> getMonthEffort(int month) {
        List<EffortInfo> monthList = new ArrayList<>();
        for (EffortInfo info : mYearEffort) {
            if (Integer.parseInt(info.getStartDate().split("-")[1]) == month) {
                monthList.add(info);
            }
        }
        return monthList;
    }

    private void showChart() {
        // 设置描述信息
        mChart.setDescription("");

        // 设置缩放
        mChart.setPinchZoom(false);

        mChart.setDrawBarShadow(false);

        mChart.setDrawValueAboveBar(true);

        // 设置数据
        mChart.setData(getChartData());
        mChart.setFitBars(true);

        // 设置动画
        mChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setAxisMinValue(0f);
        leftAxis.setYOffset(1f);
        leftAxis.setGranularity(1f);
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setDrawLabels(true);
        xLabels.setLabelCount(12);
        xLabels.setGranularity(1f);
        xLabels.setDrawGridLines(false);
        xLabels.setValueFormatter(new XAxisValueFormatter());

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
        l.setForm(Legend.LegendForm.SQUARE);
        l.setFormSize(9f);
        l.setTextSize(12f);
        l.setXEntrySpace(4f);
    }

    private BarData getChartData() {

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();

        ArrayList<BarEntry> yValues = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            yValues.add(new BarEntry(i, mChartData[i - 1]));
        }

        BarDataSet dataSet = new BarDataSet(yValues,
                getResources().getString(R.string.report_chart_title));
        dataSet.setColor(getResources().getColor(R.color.colorAll));
        dataSet.setLabel(getResources().getString(R.string.report_chart_title));
        dataSets.add(dataSet);

// 进度展示效果
//        ArrayList<BarEntry> yValues1 = new ArrayList<>();
//        yValues1.add(new BarEntry(1, 5f));
//        yValues1.add(new BarEntry(2, 3f));
//        yValues1.add(new BarEntry(3, 5f));
//        yValues1.add(new BarEntry(4, 3f));
//        yValues1.add(new BarEntry(5, 5f));
//        yValues1.add(new BarEntry(6, 3f));
//        yValues1.add(new BarEntry(7, 5f));
//        yValues1.add(new BarEntry(8, 3f));
//        yValues1.add(new BarEntry(9, 5f));
//        yValues1.add(new BarEntry(10, 3f));
//        yValues1.add(new BarEntry(11, 5f));
//        yValues1.add(new BarEntry(12, 3f));
//        BarDataSet dataSet1 = new BarDataSet(yValues1,
//                getResources().getString(R.string.report_chart_title));

//        dataSet1.setColor(getResources().getColor(R.color.colorAll));
//        dataSet1.setLabel(getResources().getString(R.string.complete_number));
//        dataSets.add(dataSet1);


        BarData data = new BarData(dataSets);
        data.setValueFormatter(new BarValueFormatter());

        // 缩小bar的宽度来控制bar之间的间隔。
        data.setBarWidth(0.8f);

        // 分组例子。
        // barWidth = 0.4f groupSpace = 0.05f barSpace = 0.1f
        // (0.4f + 0.05f) * 2 + 0.1f = 1.00 interval per "group"
        // mChart.getXAxis().setAxisMinValue(1f);
        // mChart.getXAxis().setAxisMaxValue(13f);
        // mChart.getXAxis().setCenterAxisLabels(true);
        // data.groupBars(1f, 0.1f, 0.05f);

        return data;
    }

    @Override
    public void onClick(View v) {
        switch (mCurrentButton.getId()) {
            case R.id.imageButton1:
                mCurrentButton.setBackgroundResource(R.drawable.month1);
                break;
            case R.id.imageButton2:
                mCurrentButton.setBackgroundResource(R.drawable.month2);
                break;
            case R.id.imageButton3:
                mCurrentButton.setBackgroundResource(R.drawable.month3);
                break;
            case R.id.imageButton4:
                mCurrentButton.setBackgroundResource(R.drawable.month4);
                break;
            case R.id.imageButton5:
                mCurrentButton.setBackgroundResource(R.drawable.month5);
                break;
            case R.id.imageButton6:
                mCurrentButton.setBackgroundResource(R.drawable.month6);
                break;
            case R.id.imageButton7:
                mCurrentButton.setBackgroundResource(R.drawable.month7);
                break;
            case R.id.imageButton8:
                mCurrentButton.setBackgroundResource(R.drawable.month8);
                break;
            case R.id.imageButton9:
                mCurrentButton.setBackgroundResource(R.drawable.month9);
                break;
            case R.id.imageButton10:
                mCurrentButton.setBackgroundResource(R.drawable.month10);
                break;
            case R.id.imageButton11:
                mCurrentButton.setBackgroundResource(R.drawable.month11);
                break;
            case R.id.imageButton12:
                mCurrentButton.setBackgroundResource(R.drawable.month12);
                break;
        }

        mCurrentButton = (ImageButton) v;
        ReportEffortListAdapter adapter;

        switch (v.getId()) {
            case R.id.imageButton1:
                v.setBackgroundResource(R.drawable.month1_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(1));
                break;
            case R.id.imageButton2:
                v.setBackgroundResource(R.drawable.month2_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(2));
                break;
            case R.id.imageButton3:
                v.setBackgroundResource(R.drawable.month3_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(3));
                break;
            case R.id.imageButton4:
                v.setBackgroundResource(R.drawable.month4_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(4));
                break;
            case R.id.imageButton5:
                v.setBackgroundResource(R.drawable.month5_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(5));
                break;
            case R.id.imageButton6:
                v.setBackgroundResource(R.drawable.month6_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(6));
                break;
            case R.id.imageButton7:
                v.setBackgroundResource(R.drawable.month7_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(7));
                break;
            case R.id.imageButton8:
                v.setBackgroundResource(R.drawable.month8_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(8));
                break;
            case R.id.imageButton9:
                v.setBackgroundResource(R.drawable.month9_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(9));
                break;
            case R.id.imageButton10:
                v.setBackgroundResource(R.drawable.month10_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(10));
                break;
            case R.id.imageButton11:
                v.setBackgroundResource(R.drawable.month11_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(11));
                break;
            case R.id.imageButton12:
                v.setBackgroundResource(R.drawable.month12_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(12));
                break;
            default:
                v.setBackgroundResource(R.drawable.month1_press);
                adapter = new ReportEffortListAdapter(ReportActivity.this, getMonthEffort(1));

        }

        mListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class XAxisValueFormatter implements AxisValueFormatter {

        private FormattedStringCache.PrimFloat mFormattedStringCache;

        public XAxisValueFormatter() {
            mFormattedStringCache = new FormattedStringCache.PrimFloat(new DecimalFormat("###,###,###"));
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormattedStringCache.getFormattedValue(value) + getResources().getString(R.string.month);
        }

        @Override
        public int getDecimalDigits() {
            return 1;
        }
    }

    public class BarValueFormatter implements ValueFormatter {

        private FormattedStringCache.PrimIntFloat mFormattedStringCache;

        public BarValueFormatter() {
            mFormattedStringCache = new FormattedStringCache.PrimIntFloat(new DecimalFormat("######"));
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormattedStringCache.getFormattedValue(value, dataSetIndex);
        }
    }

}
