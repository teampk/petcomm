package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;

import android.os.Handler;
import android.preference.PreferenceManager;
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
import com.example.petcomm.utils.Constants;

import de.mateware.snacky.Snacky;

public class SignInActivity extends AppCompatActivity {

    ActivitySignInBinding binding;
    private static String TAG = "PetCommTest";
    private SharedPreferences mSharedPreferences;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_in);
        final View mView = binding.getRoot();
        binding.setSignIn(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

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
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString(Constants.EMAIL, binding.etId.getText().toString());
            editor.apply();


            if(binding.cbAutoSignIn.isChecked()){
                showSnackBarMessage("로그인 되었습니다 (자동로그인)");
            }else{
                showSnackBarMessage("로그인 되었습니다");
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
        showSnackBarMessage("준비 중입니다...");

    }

    public void languageButtonListener(View view){
        showSnackBarMessage("KOREAN");

    }

    private boolean checkSignIn(){

        if (binding.etId.length() == 0){
            showSnackBarMessage("아이디를 입력해주세요.");
            return false;
        }/*
        else if (binding.etPw.length() == 0){
            Toast.makeText(this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }
        */

        return true;
    }
    private void showSnackBarMessage(String message){
        Snacky.builder()
                .setActivity(this)
                .setBackgroundColor(getColor(R.color.colorPrimary))
                .setText(message)
                .setTextColor(getColor(R.color.colorBlack))
                .centerText()
                .setDuration(Snacky.LENGTH_LONG)
                .build()
                .show();
    }
}
