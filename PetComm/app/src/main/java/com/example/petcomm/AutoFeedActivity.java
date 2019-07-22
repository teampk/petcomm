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

        //TEST
        Log.d("TestPaeng", "-----------------------------------");
        Log.d("TestPaeng", "size:"+feedScheduleList.size());

        for(int i=0;i<feedScheduleList.size();i++){
            Log.d("TestPaeng", "   ");
            Log.d("TestPaeng", String.valueOf(feedScheduleList.get(i).getmId()));
            Log.d("TestPaeng", String.valueOf(feedScheduleList.get(i).getmFeederId()));
            Log.d("TestPaeng", String.valueOf(feedScheduleList.get(i).getmFeedTime()));
            Log.d("TestPaeng", String.valueOf(feedScheduleList.get(i).getmFeedAmount()));
        }
        ////


        binding.recyclerSchedule.setHasFixedSize(true);
        binding.recyclerSchedule.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        binding.recyclerSchedule.scrollToPosition(0);

        RecyclerCustomAdapter mAdapter = new RecyclerCustomAdapter(getApplicationContext(), feedScheduleList);
        binding.recyclerSchedule.setAdapter(mAdapter);
        binding.recyclerSchedule.setItemAnimator(new DefaultItemAnimator());

    }

    public void addListListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(AutoFeedActivity.this);
        customDialogFeed.callFunction(2, "배식 일정 추가", "설정", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedTime, String feedAmount) {
                Toast.makeText(AutoFeedActivity.this, feedTime+"//"+feedAmount, Toast.LENGTH_SHORT).show();
                dbHelper.addFeederSchedule(selectedDog.feederId, feedTime, feedAmount);

            }

            @Override
            public void onNegativeClicked() {


            }
        });
    }

    public void testListener(View view){
        ArrayList<FeedSchedule> scheduleArrayList = dbHelper.getScheduleData();
        Log.d("TestPaeng", "size:"+scheduleArrayList.size());

        for(int i=0;i<scheduleArrayList.size();i++){
            Log.d("TestPaeng", String.valueOf(scheduleArrayList.get(i).getmId()));
            Log.d("TestPaeng", String.valueOf(scheduleArrayList.get(i).getmFeederId()));
            Log.d("TestPaeng", String.valueOf(scheduleArrayList.get(i).getmFeedTime()));
            Log.d("TestPaeng", String.valueOf(scheduleArrayList.get(i).getmFeedAmount()));
        }
    }
}
