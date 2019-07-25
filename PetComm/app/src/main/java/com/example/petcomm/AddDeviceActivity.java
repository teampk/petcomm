package com.example.petcomm;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAddDeviceBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddDeviceActivity extends AppCompatActivity {

    ActivityAddDeviceBinding binding;

    private int deviceMode;
    private Dog selectedDog;
    private static final int DISPLAY_LENGTH = 1000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device);
        binding.setAddDevice(this);

        Intent intent = getIntent();
        // deviceMode=1 : 급식기
        // deviceMode=2 : 배변판
        deviceMode  = intent.getIntExtra("mode", 1);
        selectedDog = (Dog) intent.getSerializableExtra("dog");

        if(deviceMode==2){
            binding.ivDevice.setImageResource(R.drawable.img_toilet);
        }


    }
    @Override
    public void onResume(){
        super.onResume();
        //registerReceiver(mReceiver, mIntentFilter);
    }

    @Override
    public void onPause(){
        super.onPause();
        // unregisterReceiver(mReceiver);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
    }


    public void registerDeviceListener(View view){
        if(deviceMode==1){
            Intent intentFeeder = new Intent(getApplicationContext(), AddDeviceActivity2.class);
            intentFeeder.putExtra("mode", 1);
            intentFeeder.putExtra("dog", selectedDog);
            binding.pbDevice.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(intentFeeder);
                    binding.pbDevice.setVisibility(View.INVISIBLE);
                    finish();
                }
            }, DISPLAY_LENGTH);

        }
        else if(deviceMode==2){
            Intent intentToilet = new Intent(getApplicationContext(), AddDeviceActivity2.class);
            intentToilet.putExtra("mode", 2);
            intentToilet.putExtra("dog", selectedDog);
            binding.pbDevice.setVisibility(View.VISIBLE);
            new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    startActivity(intentToilet);
                    binding.pbDevice.setVisibility(View.INVISIBLE);
                    finish();
                }
            }, DISPLAY_LENGTH);
        }
    }

}
