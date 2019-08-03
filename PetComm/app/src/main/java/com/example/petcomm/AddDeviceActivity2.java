package com.example.petcomm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
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

import java.io.EOFException;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    private String generatedDeviceId;


    public SendData mSendData;

    private String connectWifiId, connectWifiPw;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_device2);
        binding.setAddDevice2(this);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mSubscriptions = new CompositeSubscription();


        Intent intent = getIntent();
        // deviceMode=1 : 급식기
        // deviceMode=2 : 배변판
        deviceMode  = intent.getIntExtra("mode", 1);
        selectedDog = (Dog) intent.getSerializableExtra("dog");

        if(deviceMode==1){
            binding.ivDevice.setImageResource(R.drawable.img_feeder);
            generatedDeviceId = "f_"+getRandomString(8);
        }else if (deviceMode==2){
            binding.ivDevice.setImageResource(R.drawable.img_toilet);
            generatedDeviceId = "t_"+getRandomString(8);
        }
        ////// 지워
        binding.etWifiId.setText("Trojan.proxy.41241.5");
        binding.etWifiPw.setText("pkpk9596");
        //////



    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    class SendData extends Thread{
        public void run(){
            try{
                DatagramSocket socket = new DatagramSocket();
                InetAddress serverAddr = InetAddress.getByName(Constants.DEVICE_IP);
                connectWifiId = binding.etWifiId.getText().toString();
                connectWifiPw = binding.etWifiPw.getText().toString();
                //String data = binding.etWifiId.getText().toString()+"/"+binding.etWifiPw.getText().toString()+"/"+generatedDeviceId;
                String data = connectWifiId+"/"+connectWifiPw;

                // Send
                byte[] bufSend = (data).getBytes();
                DatagramPacket packetSend = new DatagramPacket(bufSend, bufSend.length, serverAddr, Constants.DEVICE_PORT);

                socket.send(packetSend);
                Log.d("PAENG_2_wifi_direct", "send");

                // Receive
                byte[] bufReceive = new byte[1024];
                DatagramPacket packetReceive = new DatagramPacket(bufReceive, bufReceive.length, serverAddr, Constants.DEVICE_PORT);

                socket.receive(packetReceive);
                String msg = new String(packetReceive.getData());

                Log.d("PAENG_2_wifi_direct", "receive");
                Log.d("PAENG_2_wifi_direct", msg);
                Log.d("PAENG_2_wifi_direct", "--- wifi direct finished ---");


                if(msg.split("/")[0].equals("WIN")){
                    Log.d("PAENG_2_wifi", "Connect to " + connectWifiId +" // "+ connectWifiPw);
                    connectToWifi(connectWifiId, connectWifiPw);

                    Intent intent = new Intent(getApplicationContext(), AddDeviceActivity3.class);
                    intent.putExtra("dog", selectedDog);
                    intent.putExtra("mode", deviceMode);
                    intent.putExtra("deviceId", generatedDeviceId);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(AddDeviceActivity2.this, "RESPONSE ERROR :(", Toast.LENGTH_SHORT).show();
                }

            }catch (Exception e){
                Log.d("PAENG_2_ERROR", String.valueOf(e));

            }
        }
    }


    // 기기 등록 버튼
    public void registerDeviceListener(View view){
        binding.pbDevice.setVisibility(View.VISIBLE);
        Log.d("PAENG_2_wifi_direct", "--- wifi direct ---");
        if(checkRegister()){
            mSendData = new SendData();
            mSendData.start();
        }
    }


    private boolean checkRegister(){
        if(binding.etWifiId.length()==0){
            Toast.makeText(this, "wifi id를 입력해주세요.", Toast.LENGTH_SHORT).show();
            binding.pbDevice.setVisibility(View.INVISIBLE);
            return false;
        }else if(binding.etWifiPw.length()==0){
            Toast.makeText(this, "wifi pw를 입력해주세요.", Toast.LENGTH_SHORT).show();
            binding.pbDevice.setVisibility(View.INVISIBLE);
            return false;
        }else if(!getConnectedWiFiSSID().equals("\""+Constants.WIFI_ID+"\"")){
            Toast.makeText(this, "PETCOMM Wifi로 다시 연결해서 실행해주세요!", Toast.LENGTH_SHORT).show();
            binding.pbDevice.setVisibility(View.INVISIBLE);
            return false;
        }else{
            return true;
        }

    }


    public String getConnectedWiFiSSID() {
        WifiManager wifiManager = (WifiManager)getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
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

    public void testListener(View view){
        Log.d("PAENG_2_currentWifi", getConnectedWiFiSSID());
    }

}
