package com.example.petcomm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAddDevice3Binding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.List;
import java.util.Random;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddDeviceActivity3 extends AppCompatActivity {

    private static final int DISPLAY_LENGTH = 3000;

    ActivityAddDevice3Binding binding;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CompositeSubscription mSubscriptions;


    private int deviceMode;
    private String deviceId;
    private Dog selectedDog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device3);
        binding.setAddDevice3(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSubscriptions = new CompositeSubscription();



        Intent intent = getIntent();
        // deviceMode=1 : 급식기
        // deviceMode=2 : 배변판
        deviceMode  = intent.getIntExtra("mode", 1);
        deviceId = intent.getStringExtra("deviceId");
        selectedDog = (Dog) intent.getSerializableExtra("dog");
        binding.pbWifi.setVisibility(View.VISIBLE);
        binding.btRegister.setBackgroundResource(R.drawable.layout_rounded_gray);
        binding.btRegister.setText("준비 중...");
        binding.btRegister.setClickable(false);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                binding.pbWifi.setVisibility(View.INVISIBLE);
                binding.btRegister.setBackgroundResource(R.drawable.layout_rounded_primary);
                binding.btRegister.setText("완료");
                binding.btRegister.setClickable(true);

            }
        }, 5000);




    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }




    // 기기 등록 버튼
    public void registerDeviceListener(View view){
        Log.d("PAENG_3_deviceid", deviceId);
        binding.pbDevice.setVisibility(View.VISIBLE);
        if(deviceMode==1) {
            selectedDog.feederId = deviceId;
            mSubscriptions.add(NetworkUtil.getRetrofit().registerFeeder(selectedDog.dogId, selectedDog)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse,this::handleError));
        }
        else if (deviceMode == 2) {
            selectedDog.toiletId = deviceId;
            mSubscriptions.add(NetworkUtil.getRetrofit().registerToilet(selectedDog.dogId, selectedDog)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse,this::handleError));
        }

    }



    private void handleResponse(Res response){
        binding.pbDevice.setVisibility(View.INVISIBLE);
        Toast.makeText(AddDeviceActivity3.this, "기기가 성공적으로 등록되었습니다 :)", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "NETWORK ERROR :(", Toast.LENGTH_SHORT).show();
        }
    }


}
