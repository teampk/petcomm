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
        Toast.makeText(this, "강아지 등록 완료\n다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
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
        Toast.makeText(this,"강아지 등록을 취소하려면 한번 더 눌러주세요.\n\n강아지 등록은 나중에 할 수 있지만\n기기와 연동하여 어플을 사용하려면\n강아지 등록이 필요합니다.",Toast.LENGTH_SHORT).show();
        pressTime = System.currentTimeMillis();

    }

}
