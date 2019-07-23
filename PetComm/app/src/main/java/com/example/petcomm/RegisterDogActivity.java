package com.example.petcomm;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityRegisterDogBinding;

import java.util.Calendar;
import java.util.Date;

public class RegisterDogActivity extends AppCompatActivity {

    ActivityRegisterDogBinding binding;
    DBHelper dbHelper;
    private String gender;
    private SharedPreferences mSharedPreferences;
    private String mEmail;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_dog);
        binding.setSignUpDog(this);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");

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
    public void selectBreedsListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(RegisterDogActivity.this);
        customDialogFeed.callFunction(3, "품종을 선택해주세요", "선택", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String selectedBreeds, String none) {
                binding.tvBreeds.setText(selectedBreeds);
            }

            @Override
            public void onNegativeClicked() {

            }
        });
    }
    public void selectBirthListener(View view){
        final Calendar cal = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(RegisterDogActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String monthString, dayString;
                if (month<10){
                    monthString = "0"+(month+1);
                }else{
                    monthString = String.valueOf(month+1);
                }if (dayOfMonth<10){
                    dayString = "0"+dayOfMonth;
                }else{
                    dayString = String.valueOf(month);
                }
                binding.tvBirth.setText(String.valueOf(year+"/"+monthString+"/"+dayString));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();


    }


    public void submitButtonListener(View view){
        if(checkRegister()){

            dbHelper.addDog(binding.etName.getText().toString(), gender, binding.tvBreeds.getText().toString(),
                    binding.tvBirth.getText().toString(), binding.etWeight.getText().toString(), mEmail, "", "");
            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putInt(Constants.DOG, dbHelper.getHightestDogId());
            mEditor.apply();
            Toast.makeText(this, "강아지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }

    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"강아지 등록 취소",Toast.LENGTH_SHORT).show();
        finish();
    }

    private boolean checkRegister(){
        if(binding.etName.length() == 0){
            Toast.makeText(this, "이름을 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (!binding.rbMale.isChecked() && !binding.rbFemale.isChecked()){
            Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.tvBreeds.getText().toString().equals("")){
            Toast.makeText(this, "품종을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.tvBirth.getText().toString().equals("")){
            Toast.makeText(this, "생일을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if (binding.etWeight.length()==0){
            Toast.makeText(this, "무게를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        else{
            return true;
        }
    }

}
