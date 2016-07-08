package com.vigorx.effort;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import com.github.mikephil.charting.formatter.FormattedStringCache;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {
    BarChart mChart;

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
        showChart();
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
        mChart.getAxisRight().setEnabled(false);

        XAxis xLabels = mChart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);
        xLabels.setDrawLabels(true);
        xLabels.setLabelCount(12);
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
        ArrayList<BarEntry> yValues = new ArrayList<>();
        yValues.add(new BarEntry(1, new float[]{3f, 8f}));
        yValues.add(new BarEntry(2, new float[]{8f, 9f}));
        yValues.add(new BarEntry(3, new float[]{7f, 12f}));
        yValues.add(new BarEntry(4, new float[]{4f, 5f}));
        yValues.add(new BarEntry(5, new float[]{3f, 4f}));
        yValues.add(new BarEntry(6, new float[]{7f, 8f}));
        yValues.add(new BarEntry(7, new float[]{3f, 7f}));
        yValues.add(new BarEntry(8, new float[]{5f, 9f}));
        yValues.add(new BarEntry(9, new float[]{3f, 3f}));
        yValues.add(new BarEntry(10, new float[]{2f, 5f}));
        yValues.add(new BarEntry(11, new float[]{5f, 7f}));
        yValues.add(new BarEntry(12, new float[]{3f, 4f}));

        BarDataSet dataSet = new BarDataSet(yValues,
                getResources().getString(R.string.report_chart_title));

        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(getResources().getColor(R.color.colorComplete));
        colors.add(getResources().getColor(R.color.colorAll));
        dataSet.setColors(colors);

        String[] labels = {getResources().getString(R.string.complete_number),
                getResources().getString(R.string.all_number)};
        dataSet.setStackLabels(labels);

        ArrayList<IBarDataSet> dataSets = new ArrayList<>();
        dataSets.add(dataSet);
        BarData data = new BarData(dataSets);

        // 缩小bar的宽度来控制bar之间的间隔。
        data.setBarWidth(0.9f);
        return data;
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

}
