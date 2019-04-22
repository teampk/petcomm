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
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    private static String TAG = "PetCommTest";

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        final View mView = binding.getRoot();
        binding.setSignIn(this);

        hideComponents(mView);

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                showComponents(mView);
            }
        }, 1000);

    }

    private void showComponents(View mView){

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(getApplicationContext(), R.layout.activity_sign_in_detail);

        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = mView.findViewById(R.id.cl_main);
        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint);
        //here constraint is the name of view to which we are applying the constraintSet
    }

    private void hideComponents(View mView){

        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(getApplicationContext(), R.layout.activity_sign_in);


        ChangeBounds transition = new ChangeBounds();
        transition.setInterpolator(new AnticipateOvershootInterpolator(1.0f));
        transition.setDuration(1200);
        ConstraintLayout constraint = mView.findViewById(R.id.cl_main);

        TransitionManager.beginDelayedTransition(constraint, transition);
        constraintSet.applyTo(constraint); //here constraint is the name of view to which we are applying the constraintSet
    }

    public void signInButtonListener(View view){
        if(checkSignIn()){
            if(binding.cbAutoSignIn.isChecked()){
                Toast.makeText(this, "로그인 되었습니다 (자동로그인)", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "로그인 되었습니다", Toast.LENGTH_SHORT).show();
            }
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void signUpButtonListener(View view){
        overridePendingTransition(R.anim.anim_slide_in_bottom, R.anim.anim_slide_out_top);
        startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
    }

    public void findPwButtonListener(View view){
        Toast.makeText(this, "비밀번호 찾기", Toast.LENGTH_SHORT).show();
    }

    public void languageButtonListener(View view){
        Toast.makeText(this, "언어 설정", Toast.LENGTH_SHORT).show();

    }

    private boolean checkSignIn(){
        if (binding.etId.length() == 0){
            Toast.makeText(this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (binding.etPw.length() == 0){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
