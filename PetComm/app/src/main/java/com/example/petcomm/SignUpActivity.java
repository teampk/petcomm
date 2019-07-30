package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.v7.app.AppCompatActivity;
import android.transition.ChangeBounds;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivitySignInBinding;
import com.example.petcomm.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);
        binding.setSignUp(this);
    }


    public void submitButtonListener(View view){
        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        Toast.makeText(this, "회원가입 완료\n다시 로그인 해주세요.", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"회원가입 취소",Toast.LENGTH_SHORT).show();
        overridePendingTransition(R.anim.anim_slide_in_top, R.anim.anim_slide_out_bottom);
        finish();

    }


}
