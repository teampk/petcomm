package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.model.Dog;

import java.util.ArrayList;
import java.lang.Math;
import java.util.Random;


public class FragmentDevice extends Fragment {

    FragmentDeviceBinding binding;
    boolean existFeeder = false;
    boolean existToilet = false;
    ArrayList<String> settingFeeder;
    private DBHelper dbHelper;
    private SharedPreferences mSharedPreferences;
    private int selectedDogId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentDevice(this);
        dbHelper = new DBHelper(getContext(), "PetComm.db", null, 1);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        selectedDogId = mSharedPreferences.getInt(Constants.DOG, 0);
        if (selectedDogId == 0){
            binding.llDogFalse.setVisibility(View.VISIBLE);
            binding.llDogTrue.setVisibility(View.GONE);

        }else{
            binding.llDogFalse.setVisibility(View.GONE);
            binding.llDogTrue.setVisibility(View.VISIBLE);
            setVisible();
        }

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean existDevice(String deviceId){
        return !deviceId.equals("");
    }

    public void setVisible(){
        Dog selectedDog = dbHelper.getDogById(selectedDogId);
        existFeeder = existDevice(selectedDog.feederId);
        existToilet = existDevice(selectedDog.toiletId);
        setExistDevice();


        settingFeeder = new ArrayList<>();
        settingFeeder.add("기기 설정");
        settingFeeder.add("기기 이름 변경");
        settingFeeder.add("연결 해제");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, settingFeeder);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFeeder.setAdapter(adapter);
        binding.spinnerFeeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:

                        break;
                    case 1:

                        Toast.makeText(getContext(), String.valueOf(mSharedPreferences.getInt(Constants.DOG, 0)), Toast.LENGTH_SHORT).show();
                        break;

                    case 2:
                        Toast.makeText(getContext(), "기기 해제", Toast.LENGTH_SHORT).show();

                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void addFeederListener(View view){
        // startActivity(new Intent(getContext(), AddDeviceActivity.class));
        existFeeder = true;
        setExistDevice();

    }

    public void addToiletListener(View view){
        // existToilet = true;
        // setExistDevice();
        startActivity(new Intent(getContext(), AddDeviceActivity.class));
    }

    public void feederFeedListener(View view){
        binding.pbFuncFeederFeed.setVisibility(View.VISIBLE);
        CustomDialog customDialogFeed = new CustomDialog(getContext());
        customDialogFeed.callFunction(1, "배식할 양을 설정해주세요.", "배식", "취소", binding.pbFuncFeederFeed);
    }

    public void feederFeedAutoListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(getContext());
        customDialogFeed.callFunction(2, "자동 배식 설정", "설정", "취소", binding.pbFuncFeederAutoFeed);
    }

    public void feederCameraListener(View view){
        Toast.makeText(getContext(), getRandomId(1), Toast.LENGTH_SHORT).show();

    }

    public void settingToiletListener(View view){
        Toast.makeText(getContext(), "toilet", Toast.LENGTH_SHORT).show();

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

    public void setExistDevice(){
        if(existFeeder){
            binding.clEmptyFeeder.setVisibility(View.GONE);
            binding.clExistFeeder.setVisibility(View.VISIBLE);
        }else{
            binding.clEmptyFeeder.setVisibility(View.VISIBLE);
            binding.clExistFeeder.setVisibility(View.GONE);
        }
        if(existToilet){
            binding.clEmptyToilet.setVisibility(View.GONE);
            binding.clExistToilet.setVisibility(View.VISIBLE);
        }else{
            binding.clEmptyToilet.setVisibility(View.VISIBLE);
            binding.clExistToilet.setVisibility(View.GONE);
        }
    }




}
