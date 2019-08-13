package com.example.petcomm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;

public class LineChartItem extends ChartItem {

    private final Typeface mTf;
    private int mode;
    public Context context;

    public LineChartItem(int mode, ChartData<?> cd, Context c) {
        super(cd);
        this.mode = mode;
        this.mTf = Typeface.createFromAsset(c.getAssets(), "OpenSans-Regular.ttf");
        this.context = c;
    }

    @Override
    public int getItemType() {
        return TYPE_LINECHART;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(int position, View convertView, Context c) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = LayoutInflater.from(c).inflate(R.layout.list_item_linechart, null);
            holder.chart = convertView.findViewById(R.id.chart);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }


        holder.chart.invalidate();
        holder.chart.clear();

        // apply styling
        holder.chart.getDescription().setEnabled(false);
        holder.chart.setDrawGridBackground(false);

        XAxis xAxis = holder.chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(mTf);
        xAxis.setAxisMinValue(7.01f); //experiment with these values
        xAxis.setAxisMaxValue(7.10f);
        // xAxis.setLabelCount(5);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);

        //
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(7);

        YAxis leftAxis = holder.chart.getAxisLeft();
        if (mode == 1){
            leftAxis.setAxisMaximum(100f);
        }else if (mode == 2){
            leftAxis.setAxisMaximum(50f);
        }else if (mode == 3){
            leftAxis.setAxisMaximum(100f);
        }
        leftAxis.setTypeface(mTf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = holder.chart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawAxisLine(false);
        //rightAxis.setTypeface(mTf);
        //rightAxis.setLabelCount(5, false);
        rightAxis.setDrawGridLines(false);
        //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        // set data
        holder.chart.setData((LineData) mChartData);

        // do not forget to refresh the chart
        // holder.chart.invalidate();
        holder.chart.animateX(1500);

        return convertView;
    }

    private static class ViewHolder {
        LineChart chart;
    }

    private class MyValueFormatter implements IValueFormatter {
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {

            return null;
        }
    }
    private class MyAxisValueFormatter implements IAxisValueFormatter{
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return null;
        }
    }
}
