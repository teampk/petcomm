package com.example.petcomm;

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

import java.util.ArrayList;

public class AutoFeedActivity extends AppCompatActivity {

    ActivityAutoFeedBinding binding;
    private DBHelper dbHelper;
    private SharedPreferences mSharedPreferences;
    private Dog selectedDog;
    private ArrayList<FeedSchedule> feedScheduleList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_auto_feed);
        binding.setAutoFeed(this);
        dbHelper = new DBHelper(getApplicationContext(), "PetComm.db", null, 1);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        selectedDog = dbHelper.getDogById(mSharedPreferences.getInt(Constants.DOG, 0));
        initRecyclerView();
    }
    @Override
    public void onResume(){
        super.onResume();
        initRecyclerView();
    }

    private void initRecyclerView(){

        feedScheduleList = new ArrayList<>();
        feedScheduleList = dbHelper.getScheduleData();

        binding.recyclerSchedule.setHasFixedSize(true);
        binding.recyclerSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerSchedule.scrollToPosition(0);

        RecyclerCustomAdapter mAdapter = new RecyclerCustomAdapter(getApplicationContext(), listSorting(feedScheduleList));
        binding.recyclerSchedule.setAdapter(mAdapter);
        binding.recyclerSchedule.setItemAnimator(new DefaultItemAnimator());

    }

    public ArrayList<FeedSchedule> listSorting(ArrayList<FeedSchedule> inputAl){
        ArrayList<FeedSchedule> outputAl = new ArrayList<>();
        for (int i=0; i<inputAl.size();i++){
            if(inputAl.get(i).getmFeederId().equals(selectedDog.feederId)){
                outputAl.add(inputAl.get(i));
            }
        }

        //시간 순으로 정렬
        int element1, element2;
        for(int i=0;i<outputAl.size();i++){
            for(int j=0;j<outputAl.size()-1;j++){
                element1 = Integer.valueOf(outputAl.get(j).getmFeedTime().replace(":",""));
                element2 = Integer.valueOf(outputAl.get(j+1).getmFeedTime().replace(":",""));
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
        customDialogFeed.callFunction(2, "배식 일정 추가", "설정", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedTime, String feedAmount) {
                dbHelper.addFeederSchedule(selectedDog.feederId, feedTime, feedAmount);
                initRecyclerView();
            }

            @Override
            public void onNegativeClicked() {

            }
        });
    }

    public void testListener(View view){

    }

    public void submitListener(View view){
        
    }
}
