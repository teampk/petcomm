package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivitySignUpBinding;
import com.example.petcomm.databinding.ActivitySignUpDogBinding;

public class SignUpDogActivity extends AppCompatActivity {

    ActivitySignUpDogBinding binding;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_dog);
        binding.setSignUpDog(this);

    }


    public void submitButtonListener(View view){
        Toast.makeText(this, "회원가입 완료\n다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }

    // 두번 뒤 누르면 앱 종료
    long pressTime;
    @Override
    public void onBackPressed() {

        if(System.currentTimeMillis() - pressTime <2000){
            finish();
            return;
        }
        Toast.makeText(this,"회원가입을 취소하려면 다시 한번 눌러주세요.",Toast.LENGTH_SHORT).show();
        pressTime = System.currentTimeMillis();

    }

}
