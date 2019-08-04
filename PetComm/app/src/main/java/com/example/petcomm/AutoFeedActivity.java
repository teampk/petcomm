package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.petcomm.databinding.ActivityAutoFeedBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.FeedSchedule;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

public class AutoFeedActivity extends AppCompatActivity {

    ActivityAutoFeedBinding binding;
    private SharedPreferences mSharedPreferences;
    private Dog selectedDog;
    private ArrayList<FeedSchedule> feedScheduleList;
    private boolean isEdited = false;
    private DBHelper dbHelper;

    private CompositeSubscription mSubscriptions;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_feed);
        binding.setAutoFeed(this);

        // dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        mSubscriptions = new CompositeSubscription();

        isEdited = false;
        feedScheduleList = new ArrayList<>();

        Intent intent = getIntent();
        selectedDog = (Dog) intent.getSerializableExtra("dog");
        loadSchedule(selectedDog.feederId);
    }
    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    private void loadSchedule(String feederId){
        mSubscriptions.add(NetworkUtil.getRetrofit().getFeedSchedule(feederId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseFeeder,this::handleError));
    }
    private void handleResponseFeeder(FeedSchedule[] feedSchedules){

        if(feedSchedules.length == 0){
            binding.tvScheduleEmpty.setVisibility(View.VISIBLE);
            binding.recyclerSchedule.setVisibility(View.GONE);
        }else{
            binding.tvScheduleEmpty.setVisibility(View.GONE);
            binding.recyclerSchedule.setVisibility(View.VISIBLE);
            for(FeedSchedule sitem : feedSchedules){
                if(sitem != null){
                    feedScheduleList.add(sitem);
                }
            }
            initRecyclerView();
        }
    }

    private void initRecyclerView(){
        if (feedScheduleList.size()==0){
            binding.tvScheduleEmpty.setVisibility(View.VISIBLE);
            binding.recyclerSchedule.setVisibility(View.GONE);
        }else{
            binding.tvScheduleEmpty.setVisibility(View.GONE);
            binding.recyclerSchedule.setVisibility(View.VISIBLE);
            binding.recyclerSchedule.setHasFixedSize(true);
            binding.recyclerSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            binding.recyclerSchedule.scrollToPosition(0);

            RecyclerCustomAdapter mAdapter = new RecyclerCustomAdapter(getApplicationContext(), listSorting(feedScheduleList), 1);
            binding.recyclerSchedule.setAdapter(mAdapter);
            binding.recyclerSchedule.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public ArrayList<FeedSchedule> listSorting(ArrayList<FeedSchedule> inputAl){
        ArrayList<FeedSchedule> outputAl = new ArrayList<>();
        for (int i=0; i<inputAl.size();i++){
            if(inputAl.get(i).getFeederId().equals(selectedDog.feederId)){
                outputAl.add(inputAl.get(i));
            }
        }
        //시간 순으로 정렬
        int element1, element2;
        for(int i=0;i<outputAl.size();i++){
            for(int j=0;j<outputAl.size()-1;j++){
                element1 = Integer.valueOf(outputAl.get(j).getFeedTime().replace(":",""));
                element2 = Integer.valueOf(outputAl.get(j+1).getFeedTime().replace(":",""));
                if(element1>element2){
                    FeedSchedule buffer1, buffer2;
                    buffer1 = outputAl.get(j);
                    buffer2 = outputAl.get(j+1);
                    outputAl.set(j, buffer2);
                    outputAl.set(j+1, buffer1);
                }
            }
        }
        return outputAl;
    }

    public void addListListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(AutoFeedActivity.this);
        customDialogFeed.callFunction(2, "배식 일정 추가", "추가", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedTime, String feedAmount) {
                int canAdd = 1;
                // 동일한 시간이 있는 경우
                for(FeedSchedule scheduleElement:feedScheduleList){
                    if(feedTime.equals(scheduleElement.getFeedTime())){
                        canAdd = 2;
                    }
                }
                // 스케줄이 10개 이상인 경우
                if(feedScheduleList.size()>=10){
                    canAdd = 3;
                }
                if(canAdd == 1){
                    feedScheduleList.add(new FeedSchedule(0, selectedDog.feederId, 0, feedTime, feedAmount));
                    isEdited = true;
                    initRecyclerView();
                }else if(canAdd == 2){
                    Toast.makeText(AutoFeedActivity.this, "동일한 시간에 여러번의 배식은 불가능합니다.", Toast.LENGTH_SHORT).show();
                }else if(canAdd == 3){
                    Toast.makeText(AutoFeedActivity.this, "배식 스케줄은 10개까지 가능합니다.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNegativeClicked() {

            }
        });
    }

    public void testListener(View view){

    }

    public void submitListener(View view){

        if(isEdited){
            // 지우고 새로 다 추가
            setFeedScheduleList(selectedDog);
        }

        finish();
    }
    private void setFeedScheduleList(Dog dog){
        mSubscriptions.add(NetworkUtil.getRetrofit().removeSchedule(dog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseRemove,this::handleError));
    }

    private void handleResponseRemove(Res response) {
        // Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        int i =0;
        //Register
        for(FeedSchedule fsItem:listSorting(feedScheduleList)){
            i++;
            fsItem.setFeedOrder(i);
            registerFeedScheduleList(fsItem);
        }

    }
    private void registerFeedScheduleList(FeedSchedule feedSchedule){
        mSubscriptions.add(NetworkUtil.getRetrofit().registerSchedule(feedSchedule)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseRegister,this::handleError));
    }
    private void handleResponseRegister(Res response) {
        // Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "수정 되었습니다.", Toast.LENGTH_SHORT).show();
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

    public void resetListener(View view){
        resetScheduleList(selectedDog);
    }
    private void resetScheduleList(Dog dog){
        mSubscriptions.add(NetworkUtil.getRetrofit().removeSchedule(dog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseReset,this::handleError));
    }
    private void handleResponseReset(Res response) {
        feedScheduleList.clear();
        Toast.makeText(this, "초기화 되었습니다", Toast.LENGTH_SHORT).show();
        loadSchedule(selectedDog.feederId);
    }
    public void checkProperFeedListener(View view){
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AutoFeedActivity.this, CheckProperFeedActivity.class));
    }

}
