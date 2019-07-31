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

import com.example.petcomm.databinding.ActivityAddDevice2Binding;
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

public class AddDeviceActivity2 extends AppCompatActivity {

    private static final int DISPLAY_LENGTH = 1000;

    ActivityAddDevice2Binding binding;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private CompositeSubscription mSubscriptions;


    private int deviceMode;

    private Dog selectedDog;

    WifiManager wifiManager;
    WifiP2pManager mManager;
    WifiP2pManager.Channel mChannel;
    BroadcastReceiver mReceiver;
    IntentFilter mIntentFilter;

    List<WifiP2pDevice> peers = new ArrayList<WifiP2pDevice>();
    String[] deviceNameArray;
    WifiP2pDevice[] deviceArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device2);
        binding.setAddDevice2(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSubscriptions = new CompositeSubscription();

        Intent intent = getIntent();
        // deviceMode=1 : 급식기
        // deviceMode=2 : 배변판
        deviceMode  = intent.getIntExtra("mode", 1);
        selectedDog = (Dog) intent.getSerializableExtra("dog");

        if(deviceMode==2){
            binding.ivDevice.setImageResource(R.drawable.img_toilet);
        }
        binding.etWifiId.setText("Trojan.proxy.42141.2.4");





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
        mSubscriptions.unsubscribe();
    }


    // 서버에 기기 등록
    public void registerDeviceListener(View view){
        binding.pbDevice.setVisibility(View.VISIBLE);

        if(deviceMode==1){
            selectedDog.feederId = "f_"+getRandomString(8);
            Log.d("generated FeederID", selectedDog.feederId);
            mSubscriptions.add(NetworkUtil.getRetrofit().registerFeeder(selectedDog.dogId, selectedDog)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse,this::handleError));
        }
        else if(deviceMode==2){
            selectedDog.toiletId = "t_"+getRandomString(8);
            mSubscriptions.add(NetworkUtil.getRetrofit().registerToilet(selectedDog.dogId, selectedDog)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse,this::handleError));
        }
    }

    private void handleResponse(Res response){
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                binding.pbDevice.setVisibility(View.INVISIBLE);
                Toast.makeText(AddDeviceActivity2.this, "기기가 성공적으로 등록되었습니다 :)", Toast.LENGTH_SHORT).show();
                finish();
            }
        }, DISPLAY_LENGTH);
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
    private static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = ("A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z," +
                "a,b,c,d,e,f,g,h,i,j,k,l,m,n,o,p,q,r,s,t,u,v,w,x,y,z," +
                "1,2,3,4,5,6,7,8,9,0").split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

}
