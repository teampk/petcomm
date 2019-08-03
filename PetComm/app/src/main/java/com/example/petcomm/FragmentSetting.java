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
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.databinding.FragmentSettingBinding;

public class FragmentSetting extends Fragment {

    FragmentSettingBinding binding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentSetting(this);

        return mView;
    }

    public void deleteData(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("모든 데이터를 삭제합니다");
        builder.setMessage("앱 내에서 추가한 모든 일정 데이터를 삭제합니다.\n계속하시겠습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        builder.show();

    }
    public void signOutListener(View view){
        startActivity(new Intent(getContext(), SignInActivity.class));
        getActivity().finish();
    }




}
