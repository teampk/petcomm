package com.example.petcomm;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
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
import java.util.Vector;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AddDeviceActivity extends AppCompatActivity {


    private int deviceMode;
    private Dog selectedDog;
    private static final int DISPLAY_LENGTH = 3000;

    Vector<AccessPoint> accessPoints;
    LinearLayoutManager linearLayoutManager;
    AccessPointAdapter accessPointAdapter;
    WifiManager wifiManager;
    WifiInfo wifiInfo;
    List<ScanResult> scanResult;
    ActivityAddDeviceBinding binding;

    private SharedPreferences mSharedPreferences;

    public static final int MULTIPLE_PERMISSIONS = 10;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    @Override
    protected void onStart(){
        super.onStart();
        if(Build.VERSION.SDK_INT >=23){
            if(!checkPermissions()){
                finish();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device);
        binding.setAddDevice(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putString(Constants.WIFI, "");
        mEditor.apply();


        Intent intent = getIntent();
        // deviceMode=1 : 급식기
        // deviceMode=2 : 배변판
        deviceMode  = intent.getIntExtra("mode", 1);
        selectedDog = (Dog) intent.getSerializableExtra("dog");

        if(deviceMode==2){
            binding.ivDevice.setImageResource(R.drawable.img_toilet);
        }

        // wifi 목록 불러오기
        LocationManager locationManager = (LocationManager)getSystemService(LOCATION_SERVICE);
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            //GPS 설정화면으로 이동
            Intent intentLoc = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intentLoc.addCategory(Intent.CATEGORY_DEFAULT);
            startActivity(intentLoc);
        }


        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        binding.accessPointRecyclerView.setLayoutManager(linearLayoutManager);
        accessPoints = new Vector<>();

        if(wifiManager != null){
            Log.d("PAENGWifi", String.valueOf(wifiManager.isWifiEnabled()));
            if(!wifiManager.isWifiEnabled()){
                wifiManager.setWifiEnabled(true);
            }
            final IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
            filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
            registerReceiver(mWifiScanReceiver, filter);
            wifiManager.startScan();
        }
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(mWifiScanReceiver);
    }

    private BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if(action!=null){
                if(action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)){
                    getWIFIScanResult();
                    wifiManager.startScan();
                }else if(action.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)){
                    context.sendBroadcast(new Intent("wifi.ON_NETWORK_STATE_CHANGED"));
                }
            }
        }
    };
    public void getWIFIScanResult(){
        scanResult = wifiManager.getScanResults();
        binding.pbWifi.setVisibility(View.INVISIBLE);
        if(accessPoints.size()!=0){
            accessPoints.clear();
        }
        for(int i =0; i<scanResult.size();i++){
            ScanResult result = scanResult.get(i);
            if(result.frequency < 6000){
                Log.d("SSID:"+result.SSID, result.level + ","+result.BSSID);
                accessPoints.add(new AccessPoint(result.SSID, result.BSSID, String.valueOf(result.level)));
            }
        }
        accessPointAdapter = new AccessPointAdapter(accessPoints, AddDeviceActivity.this);
        binding.accessPointRecyclerView.setAdapter(accessPointAdapter);
        accessPointAdapter.notifyDataSetChanged();
    }

    private boolean checkPermissions(){
        int result;
        List<String> listPermissionNeeded = new ArrayList<>();
        for(String p : permissions){
            result = ContextCompat.checkSelfPermission(AddDeviceActivity.this, p);
            if(result != PackageManager.PERMISSION_GRANTED){
                listPermissionNeeded.add(p);
            }
        }
        if(!listPermissionNeeded.isEmpty()){
            ActivityCompat.requestPermissions(AddDeviceActivity.this, listPermissionNeeded.toArray(new String[listPermissionNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults){
        switch(requestCode){
            case MULTIPLE_PERMISSIONS:{
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Log.d("PERMISSION","GRANTED");
                }
            }
        }
    }


    public void chooseCompleteListener(View view){
        String selected = "";
        selected = mSharedPreferences.getString(Constants.WIFI, "");
        if(selected.split("/")[0].equals(Constants.WIFI_ID)){
            Toast.makeText(this, "선택이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            binding.accessPointRecyclerView.setVisibility(View.GONE);
            binding.btChoose.setBackgroundResource(R.drawable.layout_rounded_gray);
            binding.btChoose.setClickable(false);
        }else{
            Toast.makeText(this, "위 리스트에서 PETCOMM을 찾아 선택해주세요!", Toast.LENGTH_SHORT).show();
        }

    }

    public void registerDeviceListener(View view){
        Intent intent = new Intent(getApplicationContext(), AddDeviceActivity2.class);
        intent.putExtra("dog", selectedDog);
        intent.putExtra("mode", deviceMode);
        binding.pbDevice.setVisibility(View.VISIBLE);
        if(binding.etPassword.getText().toString().equals(Constants.WIFI_PW)){

            connectToWifi(Constants.WIFI_ID, Constants.WIFI_PW);

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(String.valueOf(getConnectedWiFiSSID()).equals('"'+Constants.WIFI_ID+'"')){
                        binding.pbDevice.setVisibility(View.INVISIBLE);
                        Toast.makeText(AddDeviceActivity.this, "기기와 연결되었습니다.", Toast.LENGTH_SHORT).show();
                        startActivity(intent);
                        finish();

                    }else{
                        handler.postDelayed(this,1000);

                    }
                }
            }, 1000);





        }else{
            Toast.makeText(this, "비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
            binding.pbDevice.setVisibility(View.INVISIBLE);
        }

    }

    public void testListener(View view){
        Toast.makeText(this, getConnectedWiFiSSID(), Toast.LENGTH_SHORT).show();

    }

    public String getConnectedWiFiSSID() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiInfo = wifiManager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    public void connectToWifi(String id, String pw){
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = "\"" + id + "\"";
        conf.wepTxKeyIndex = 0;
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);

        conf.preSharedKey = "\"" + pw +"\"";
        conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for(WifiConfiguration i : list){
            if(i.SSID != null && i.SSID.equals("\"" + id + "\"")){
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }



}
