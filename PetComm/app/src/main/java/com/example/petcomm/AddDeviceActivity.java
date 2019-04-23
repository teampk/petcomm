package com.example.petcomm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAddDeviceBinding;

public class AddDeviceActivity extends AppCompatActivity {

    ActivityAddDeviceBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_dog);
        binding.setAddDevice(this);

    }

    public void addDeviceListener(View view){
        Toast.makeText(this, "기기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }
}
