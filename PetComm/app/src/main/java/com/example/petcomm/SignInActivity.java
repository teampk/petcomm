package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        binding.setSignin(this);
    }

    public void finishView(View view){
        finish();
    }

    public void signInListener(View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }

    public void signUpListener(View view){
        Toast.makeText(getApplicationContext(), "Sign Up Button", Toast.LENGTH_SHORT).show();
    }

    public void findPasswordListener(View view){
        Toast.makeText(getApplicationContext(), "Find Password", Toast.LENGTH_SHORT).show();
    }
}
