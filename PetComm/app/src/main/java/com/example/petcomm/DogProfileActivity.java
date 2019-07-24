package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.example.petcomm.databinding.ActivityDogProfileBinding;
import com.example.petcomm.model.Dog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DogProfileActivity extends AppCompatActivity {
    ActivityDogProfileBinding binding;
    private Dog myDog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dog_profile);
        binding.setDogProfile(this);

        Intent intent = getIntent();
        int id = intent.getIntExtra("dogId", 0);

        DBHelper dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        myDog = dbHelper.getDogById(id);

        binding.tvId.setText(String.valueOf(myDog.id));
        binding.tvName.setText(myDog.dogName);
        binding.tvGender.setText(myDog.dogGender);
        binding.tvBreeds.setText(myDog.dogBreeds);
        binding.tvAge.setText(getAgeByBirth(myDog.dogBirth));
        binding.tvBirth.setText(myDog.dogBirth);
        binding.tvWeight.setText(myDog.dogWeight);
        if(myDog.feederId.equals("")){
            binding.tvFeederId.setText(getString(R.string.tv_device_empty));
        }else{
            binding.tvFeederId.setText(myDog.feederId);
        }
        if(myDog.toiletId.equals("")){
            binding.tvToiletId.setText(getString(R.string.tv_device_empty));
        }else{
            binding.tvToiletId.setText(myDog.toiletId);
        }
    }
    public void deleteListener(View view){
        Log.d("TESTPAENG", myDog.id+"/"+myDog.userEmail+"/"+myDog.feederId+"/"+ myDog.toiletId+"/");
    }

    public String getAgeByBirth(String birth){
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy");
        String formatDate = sdfNow.format(new Date(System.currentTimeMillis()));

        String birthYear = birth.split("/")[0];

        int age = Integer.valueOf(formatDate) - Integer.valueOf(birthYear);



        return String.valueOf(age+1);
    }

}
