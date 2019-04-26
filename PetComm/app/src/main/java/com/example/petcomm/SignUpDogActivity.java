package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivitySignUpBinding;
import com.example.petcomm.databinding.ActivitySignUpDogBinding;

public class SignUpDogActivity extends AppCompatActivity {

    ActivitySignUpDogBinding binding;
    DBHelper dbHelper;
    private String gender;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up_dog);
        binding.setSignUpDog(this);

        binding.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_male:
                        gender = "male";

                    case R.id.rb_female:
                        gender = "female";

                }
            }
        });

    }


    public void submitButtonListener(View view){
        if(checkRegister()){

            dbHelper.addDog(binding.etName.getText().toString(), gender, "진돗개", "19951114", "3.5");



            Toast.makeText(this, "강아지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    // 두번 뒤 누르면 앱 종료
    long pressTime;
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"강아지 등록 취소",Toast.LENGTH_SHORT).show();
        finish();


    }
    private boolean checkRegister(){
        if(binding.etName.length() == 0){
            Toast.makeText(this, "강아지 이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!binding.rbMale.isChecked() && !binding.rbFemale.isChecked()){
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        else{
            return true;
        }
    }

}
