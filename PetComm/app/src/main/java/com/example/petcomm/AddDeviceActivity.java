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
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAddDeviceBinding;
import com.example.petcomm.utils.Constants;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AddDeviceActivity extends AppCompatActivity {

    ActivityAddDeviceBinding binding;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;
    private DBHelper dbHelper;
    private int deviceMode;

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
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device);
        binding.setAddDevice(this);
        dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        Intent intent = getIntent();
        deviceMode  = intent.getIntExtra("mode", 1);



        /*

        wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        mChannel = mManager.initialize(this, getMainLooper(), null);
        mReceiver = new WiFiDirectBroadcastReceiver(mManager, mChannel, AddDeviceActivity.this);

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

        if(wifiManager.isWifiEnabled()){
            binding.btWifi.setText("OFF");
        }else{
            binding.btWifi.setText("ON");
        }

        listviewListener();
        */


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

    WifiP2pManager.PeerListListener peerListListener = new WifiP2pManager.PeerListListener() {
        @Override
        public void onPeersAvailable(WifiP2pDeviceList peerList) {
            if(!peerList.getDeviceList().equals(peers)){
                peers.clear();
                peers.addAll(peerList.getDeviceList());

                deviceNameArray = new String[peerList.getDeviceList().size()];
                deviceArray = new WifiP2pDevice[peerList.getDeviceList().size()];
                int index = 0;

                for(WifiP2pDevice device : peerList.getDeviceList()){
                    deviceNameArray[index] = device.deviceName;
                    deviceArray[index] = device;
                    index++;
                }

                binding.listViewDevice.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, deviceNameArray));

                for(int i=0; i<deviceNameArray.length;i++){
                    binding.tvDevice.append("device added: " + deviceNameArray[i]+"\n");
                }

            }
            if (peers.size()==0){
                binding.tvDevice.append("---No Device Found---\n\n");
            }
        }
    };

    WifiP2pManager.ConnectionInfoListener connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
        @Override
        public void onConnectionInfoAvailable(WifiP2pInfo info) {
            final InetAddress groupOwnerAddress = info.groupOwnerAddress;

            if(info.groupFormed && info.isGroupOwner){
                binding.tvDevice.append("<<< HOST >>>");
            }else if (info.groupFormed){
                binding.tvDevice.append("<<< Client >>>");
            }
        }
    };

    public void listviewListener(){
        binding.listViewDevice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final WifiP2pDevice device = deviceArray[position];
                WifiP2pConfig config = new WifiP2pConfig();
                config.deviceAddress = device.deviceAddress;

                mManager.connect(mChannel, config, new WifiP2pManager.ActionListener() {
                    @Override
                    public void onSuccess() {
                        binding.tvDevice.append("Connected to "+device.deviceName+"\n\n");
                        Toast.makeText(AddDeviceActivity.this, "Connected to "+device.deviceName, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(int reason) {
                        binding.tvDevice.append("Not Connected\n\n");
                        Toast.makeText(AddDeviceActivity.this, "Not Connected", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }

    public void wifiListener(View view){
        /*
        if(wifiManager.isWifiEnabled()){
            wifiManager.setWifiEnabled(false);
            binding.btWifi.setText("WIFI ON");
        }else{
            wifiManager.setWifiEnabled(true);
            binding.btWifi.setText("WIFI OFF");
        }
        */
    }

    public void addDeviceListener(View view){
        /*
        mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                binding.tvDevice.append("---Discovery Started---\n\n");
            }

            @Override
            public void onFailure(int reason) {
                binding.tvDevice.append("---Discovery Starting Failed---\n\n");
            }
        });
        */
        if(deviceMode==1){
            dbHelper.registerFeeder(mSharedPreferences.getInt(Constants.DOG, 0), getRandomId(1));
            Toast.makeText(this, "급식기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
        else if(deviceMode==2){
            dbHelper.registerToilet(mSharedPreferences.getInt(Constants.DOG, 0), getRandomId(2));
            Toast.makeText(this, "배변판이 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    private String getRandomId(int mode){
        Random rnd = new Random();
        String result = "";
        for (int i=0; i<5; i++){
            if(rnd.nextBoolean()){
                result += (char)((int)(Math.random()*26)+65);
            }
            else{
                result += (char)((int)(Math.random()*26)+97);
            }
        }
        if (mode==1){
            return "F_"+result;
        }else{
            return "T_"+result;
        }
    }
}
