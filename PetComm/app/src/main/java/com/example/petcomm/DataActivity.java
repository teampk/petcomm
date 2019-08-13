package com.example.petcomm;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.example.petcomm.databinding.ActivityDataBinding;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import rx.subscriptions.CompositeSubscription;

public class DataActivity extends AppCompatActivity {

    private CompositeSubscription mSubscriptions;
    private ActivityDataBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_data);
        binding.setData(this);
        mSubscriptions = new CompositeSubscription();




    }

    @Override
    protected void onResume() {
        super.onResume();
        ArrayList<ChartItem> chartList = new ArrayList<>();
        chartList.add(new LineChartItem(1, generateDataLine(1), getApplicationContext()));
        chartList.add(new LineChartItem(2, generateDataLine(2), getApplicationContext()));
        chartList.add(new LineChartItem(3, generateDataLine(3), getApplicationContext()));
        ChartDataAdapter cda = new ChartDataAdapter(getApplicationContext(), chartList);
        binding.lvData.setAdapter(cda);
    }

    private LineData generateDataLine(int mode){
        ArrayList<Entry> dataVals = new ArrayList<>();


        LineDataSet mLineDataSet;
        if (mode == 1) {
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry((float)(7+(i*0.01)), (float)(Math.random() * 10) + 40));
            }

            mLineDataSet = new LineDataSet(dataVals, "먹은 사료 양 (g)");
            mLineDataSet.setHighLightColor(getColor(R.color.colorChart1));
            mLineDataSet.setCircleColor(getColor(R.color.colorChart1));
            mLineDataSet.setColor(getColor(R.color.colorChart1));
        }else if (mode==2){
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry((float)(7+(i*0.01)), (float)(Math.random() * 5) + 10));

            }
            mLineDataSet = new LineDataSet(dataVals, "체중 (kg)");
            mLineDataSet.setHighLightColor(getColor(R.color.colorChart2));
            mLineDataSet.setCircleColor(getColor(R.color.colorChart2));
            mLineDataSet.setColor(getColor(R.color.colorChart2));
        }else{
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry((float)(7+(i*0.01)), (float)(Math.random() * 10) + 30));
            }
            mLineDataSet = new LineDataSet(dataVals, "배변 양 (g)");
            mLineDataSet.setHighLightColor(getColor(R.color.colorChart3));
            mLineDataSet.setCircleColor(getColor(R.color.colorChart3));
            mLineDataSet.setColor(getColor(R.color.colorChart3));
        }

        mLineDataSet.setLineWidth(4f);
        mLineDataSet.setCircleRadius(4f);
        mLineDataSet.setDrawValues(false);

        return new LineData(mLineDataSet);

    }

    private class ChartDataAdapter extends ArrayAdapter<ChartItem> {

        ChartDataAdapter(Context context, List<ChartItem> objects) {
            super(context, 0, objects);
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, @NonNull ViewGroup parent) {
            //noinspection ConstantConditions
            return getItem(position).getView(position, convertView, getContext());
        }

        @Override
        public int getItemViewType(int position) {
            // return the views type
            ChartItem ci = getItem(position);
            return ci != null ? ci.getItemType() : 0;
        }

        @Override
        public int getViewTypeCount() {
            return 3; // we have 3 different item-types
        }
    }
}