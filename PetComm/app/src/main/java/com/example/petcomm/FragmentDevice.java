package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class FragmentDevice extends Fragment {

    FragmentDeviceBinding binding;
    boolean existFeeder = false;
    boolean existToilet = false;
    ArrayList<String> settingFeeder;

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
        setVisible();
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
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

    public void setVisible(){
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
                        Toast.makeText(getContext(), "기기 이름을 변경", Toast.LENGTH_SHORT).show();

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
        existFeeder = true;
        setExistDevice();
        // startActivity(new Intent(getContext(), AddDeviceActivity.class));
    }

    public void addToiletListener(View view){
        // existToilet = true;
        // setExistDevice();
        startActivity(new Intent(getContext(), AddDeviceActivity.class));
    }

    public void feederFeedListener(View view){
        Toast.makeText(getContext(), "feeder", Toast.LENGTH_SHORT).show();

    }

    public void feederFeedAutoListener(View view){
        Toast.makeText(getContext(), "auto feed", Toast.LENGTH_SHORT).show();
    }

    public void feederCameraListener(View view){
        Toast.makeText(getContext(), "camera", Toast.LENGTH_SHORT).show();
    }

    public void settingToiletListener(View view){
        Toast.makeText(getContext(), "toilet", Toast.LENGTH_SHORT).show();

    }




}
