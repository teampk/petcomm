package com.example.petcomm;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityDataBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.HealthEat;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.github.mikephil.charting.charts.LineChart;
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
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DataActivity extends AppCompatActivity {

    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private ActivityDataBinding binding;
    private String selectedDogId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_data);
        binding.setData(this);
        mSubscriptions = new CompositeSubscription();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedDogId = mSharedPreferences.getString(Constants.DOG, "");
        loadDogInf(selectedDogId);
        setChart(binding.chart1, 1);
        setChart(binding.chart2, 2);
        setChart(binding.chart3, 3);

    }

    private void setChart(LineChart lineChart, int mode){
        lineChart.setBackgroundColor(getColor(R.color.colorWhite));
        lineChart.getDescription().setEnabled(false);
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);
        lineChart.setPinchZoom(false);

        XAxis xAxis;
        {
            xAxis = lineChart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setAxisMinValue(1);
            xAxis.setAxisMaxValue(10);
            xAxis.setLabelCount(9);
            xAxis.setDrawAxisLine(false);
            xAxis.setDrawGridLines(false);
        }
        YAxis leftAxis;
        {
            leftAxis = lineChart.getAxisLeft();
            if (mode == 1){
                leftAxis.setAxisMaximum(90f);
            }else if (mode == 2){
                leftAxis.setAxisMaximum(50f);
            }else if (mode == 3){
                leftAxis.setAxisMaximum(90f);
            }
            leftAxis.setDrawLabels(true);
            leftAxis.setDrawAxisLine(true);
            leftAxis.setDrawGridLines(true);
            leftAxis.setLabelCount(5, false);
            leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        }
        YAxis rightAxis;
        {
            rightAxis = lineChart.getAxisRight();
            rightAxis.setDrawLabels(false);
            rightAxis.setDrawAxisLine(false);
            //rightAxis.setTypeface(mTf);
            //rightAxis.setLabelCount(5, false);
            rightAxis.setDrawGridLines(false);
            //rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        }

        lineChart.setData(generateDataLine(mode));

        lineChart.invalidate();
        lineChart.animateX(1200);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }
    private void loadDogInf(String selectedDogId){
        mSubscriptions.add(NetworkUtil.getRetrofit().getDogByDogId(selectedDogId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDog,this::handleError));
    }
    private void handleResponseDog(Dog myDog){
        if(myDog.feederId.equals("")){
            binding.tvFeeder.setText(getString(R.string.tv_feeder_empty));
            binding.chart1.setVisibility(View.GONE);
            binding.tvDataEat.setVisibility(View.GONE);
        }else{
            String placeholder = "자동급식기 : " + myDog.feederId;
            binding.tvFeeder.setText(placeholder);
            binding.chart1.setVisibility(View.VISIBLE);
            binding.tvDataEat.setVisibility(View.VISIBLE);

            // 식습관 데이터 불러오기
            loadHealthEat(myDog.feederId);

        }
        if(myDog.toiletId.equals("")){
            binding.tvToilet.setText(getString(R.string.tv_toilet_empty));
            binding.chart2.setVisibility(View.GONE);
            binding.chart3.setVisibility(View.GONE);
            binding.tvDataWeight.setVisibility(View.GONE);
            binding.tvDataPoop.setVisibility(View.GONE);
        }else{
            String placeholder = "배변키트 : " + myDog.toiletId;
            binding.tvToilet.setText(placeholder);
            binding.chart2.setVisibility(View.VISIBLE);
            binding.chart3.setVisibility(View.VISIBLE);
            binding.tvDataWeight.setVisibility(View.VISIBLE);
            binding.tvDataPoop.setVisibility(View.VISIBLE);





        }
    }
    private void loadHealthEat(String feederId){
        mSubscriptions.add(NetworkUtil.getRetrofit().getHealthEatByFeederId(feederId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseHealthEat,this::handleError));
    }
    private void handleResponseHealthEat(HealthEat[] healthEats){
        for (HealthEat h : healthEats){
            Log.d("PaengHealthData", "-----------");
            Log.d("PaengHealthData", h.getmFeederId());
            Log.d("PaengHealthData", h.getmFeedAmount());
            Log.d("PaengHealthData", h.getCreated_at());
        }

    }
    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("PaengHelathError", String.valueOf(error));
            Toast.makeText(getApplicationContext(), "Network Error :(", Toast.LENGTH_SHORT).show();
        }
    }
    private LineData generateDataLine(int mode){
        ArrayList<Entry> dataVals = new ArrayList<>();

        LineDataSet mLineDataSet;
        if (mode == 1) {
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry(i, (float)(Math.random() * 10) + 40));
            }

            mLineDataSet = new LineDataSet(dataVals, "먹은 사료 양 (g)");
            mLineDataSet.setHighLightColor(getColor(R.color.colorChart1));
            mLineDataSet.setCircleColor(getColor(R.color.colorChart1));
            mLineDataSet.setColor(getColor(R.color.colorChart1));
        }else if (mode==2){
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry(i, (float)(Math.random() * 5) + 10));

            }
            mLineDataSet = new LineDataSet(dataVals, "체중 (kg)");
            mLineDataSet.setHighLightColor(getColor(R.color.colorChart2));
            mLineDataSet.setCircleColor(getColor(R.color.colorChart2));
            mLineDataSet.setColor(getColor(R.color.colorChart2));
        }else{
            for (int i = 1; i <= 10; i++) {
                dataVals.add(new Entry(i, (float)(Math.random() * 10) + 30));
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

}