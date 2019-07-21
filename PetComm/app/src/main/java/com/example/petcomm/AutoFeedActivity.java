package com.example.petcomm;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAutoFeedBinding;

public class AutoFeedActivity extends AppCompatActivity {

    ActivityAutoFeedBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_feed);
        binding.setAutoFeed(this);

    }

    public void addListListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(AutoFeedActivity.this);
        customDialogFeed.callFunction(2, "배식 일정 추가", "설정", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedTime, String feedAmount) {
                Toast.makeText(AutoFeedActivity.this, feedTime+"//"+feedAmount, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNegativeClicked() {

            }
        });
    }
}
