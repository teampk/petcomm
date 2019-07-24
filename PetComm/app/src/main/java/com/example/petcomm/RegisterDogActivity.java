package com.example.petcomm;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityRegisterDogBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class RegisterDogActivity extends AppCompatActivity {

    ActivityRegisterDogBinding binding;
    private String mEmail;
    private String gender;
    private SharedPreferences mSharedPreferences;
    DBHelper dbHelper;
    private CompositeSubscription mSubscriptions;


    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_register_dog);
        binding.setSignUpDog(this);
        mSubscriptions = new CompositeSubscription();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        mEmail = mSharedPreferences.getString(Constants.EMAIL, "");

        binding.rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.rb_male:
                        gender = "male";
                        break;
                    case R.id.rb_female:
                        gender = "female";
                        break;
                }
            }
        });
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
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
                    dayString = String.valueOf(dayOfMonth);
                }
                binding.tvBirth.setText(String.valueOf(year+"/"+monthString+"/"+dayString));
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
        //dialog.getDatePicker().setMaxDate(new Date().getTime());
        dialog.show();


    }
    public void submitButtonListener(View view){
        if(checkRegister()){

            // 내부 DB
            dbHelper.addDog(binding.etName.getText().toString(), gender, binding.tvBreeds.getText().toString(),
                    binding.tvBirth.getText().toString(), binding.etWeight.getText().toString(), mEmail, "", "");

            // Server DB
            Dog dogDB = new Dog();
            String newDogId = getRandomString(8);
            dogDB.setDogId(newDogId);
            dogDB.setDogName(binding.etName.getText().toString());
            dogDB.setDogGender(gender);
            dogDB.setDogBreeds(binding.tvBreeds.getText().toString());
            dogDB.setDogBirth(binding.tvBirth.getText().toString());
            dogDB.setDogWeight(binding.etWeight.getText().toString());
            dogDB.setUserEmail(mEmail);
            dogDB.setFeederId("");
            dogDB.setToiletId("");
            registerProgress(dogDB);


            SharedPreferences.Editor mEditor = mSharedPreferences.edit();
            mEditor.putString(Constants.DOG, newDogId);
            mEditor.apply();
            Toast.makeText(this, "강아지가 등록되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    //// SERVER PROCESS
    private void registerProgress(Dog dogDB) {

        mSubscriptions.add(NetworkUtil.getRetrofit().registerDog(dogDB)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }
    private void handleResponse(Res response) {

        Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        Log.d("PAENGSERVER", response.getMessage());
    }

    private void handleError(Throwable error) {

        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
                Log.d("PAENGSERVER", response.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "NETWORK ERROR :(", Toast.LENGTH_SHORT).show();
            Log.d("PAENGSERVER", error.toString());
        }
    }
    ////

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"강아지 등록이 취소되었습니다.",Toast.LENGTH_SHORT).show();
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

    private static String getRandomString(int length) {
        StringBuffer buffer = new StringBuffer();
        Random random = new Random();

        String chars[] = "A,B,C,D,E,F,G,H,I,J,K,L,M,N,O,P,Q,R,S,T,U,V,W,X,Y,Z,1,2,3,4,5,6,7,8,9,0".split(",");

        for (int i=0 ; i<length ; i++)
        {
            buffer.append(chars[random.nextInt(chars.length)]);
        }
        return buffer.toString();
    }

}
