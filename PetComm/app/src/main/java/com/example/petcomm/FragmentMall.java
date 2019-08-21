package com.example.petcomm;

import android.content.DialogInterface;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.petcomm.databinding.FragmentMallBinding;
import com.example.petcomm.databinding.FragmentSettingBinding;

public class FragmentMall extends Fragment {

    FragmentMallBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mall, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentMall(this);

        return mView;
    }

}
