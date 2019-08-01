package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityDogProfileBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class DogProfileActivity extends AppCompatActivity {
    ActivityDogProfileBinding binding;
    private CompositeSubscription mSubscriptions;
    private int pictureMode;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dog_profile);
        binding.setDogProfile(this);
        mSubscriptions = new CompositeSubscription();

        Intent intent = getIntent();
        loadDogInf(intent.getStringExtra("dogId"));
        pictureMode = intent.getIntExtra("pictureMode", 1);
        switch(pictureMode){
            case 1:
                binding.ivProfile.setImageResource(R.drawable.dog_example1);
                break;
            case 2:
                binding.ivProfile.setImageResource(R.drawable.dog_example2);
                break;
            case 3:
                binding.ivProfile.setImageResource(R.drawable.dog_example3);
                break;
            case 4:
                binding.ivProfile.setImageResource(R.drawable.dog_example4);
                break;
            default:
                binding.ivProfile.setImageResource(R.drawable.ic_dog);
                break;
        }

    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    private void loadDogInf(String selectedDogId){
        mSubscriptions.add(NetworkUtil.getRetrofit().getDogByDogId(selectedDogId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDog,this::handleError));
    }
    private void handleResponseDog(Dog myDog){
        binding.tvId.setText(myDog.dogId);
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
    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(getApplicationContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Network Error :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteListener(View view){

    }

    public String getAgeByBirth(String birth){
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy");
        String formatDate = sdfNow.format(new Date(System.currentTimeMillis()));
        String birthYear = birth.split("/")[0];
        int age = Integer.valueOf(formatDate) - Integer.valueOf(birthYear);
        return String.valueOf(age+1);
    }
}
