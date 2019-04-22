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
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        Toast.makeText(this, "강아지 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }

}
