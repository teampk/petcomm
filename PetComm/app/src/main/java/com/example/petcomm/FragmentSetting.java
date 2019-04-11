package com.example.petcomm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.databinding.FragmentSettingBinding;

public class FragmentSetting extends Fragment {

    FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        final View mView = binding.getRoot();


        return mView;
    }




}
