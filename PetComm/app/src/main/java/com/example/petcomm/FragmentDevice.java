package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.databinding.FragmentHomeBinding;

public class FragmentDevice extends Fragment {

    FragmentDeviceBinding binding;
    boolean existFeeder = false;
    boolean existToilet = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentDevice(this);
        setExistDevice();


        return mView;
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
