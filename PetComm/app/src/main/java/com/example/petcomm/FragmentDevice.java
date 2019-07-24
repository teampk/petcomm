package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentDeviceBinding;
import com.example.petcomm.model.Dog;
import com.example.petcomm.model.FeedSchedule;
import com.example.petcomm.model.Res;
import com.example.petcomm.network.NetworkUtil;
import com.example.petcomm.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.adapter.rxjava.HttpException;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;


public class FragmentDevice extends Fragment {

    FragmentDeviceBinding binding;
    private DBHelper dbHelper;
    private SharedPreferences mSharedPreferences;
    private CompositeSubscription mSubscriptions;
    private String selectedDogId;
    private Dog selectedDog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentDevice(this);
        dbHelper = new DBHelper(getContext(), "PetComm.db", null, 1);
        mSubscriptions = new CompositeSubscription();

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        selectedDogId = mSharedPreferences.getString(Constants.DOG, "");
        Log.d("PAENG_SHARED_DOG", selectedDogId);

        // 강아지가 선택되지 않았을 때.
        if (selectedDogId.equals("")){
            binding.llDogFalse.setVisibility(View.VISIBLE);
            binding.llDogTrue.setVisibility(View.GONE);
        }
        // 선택된 강아지가 있을 때.
        else{
            binding.llDogFalse.setVisibility(View.GONE);
            binding.llDogTrue.setVisibility(View.VISIBLE);
            setVisible();
        }
        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (selectedDogId.equals("")){
            binding.llDogFalse.setVisibility(View.VISIBLE);
            binding.llDogTrue.setVisibility(View.GONE);

        }else{
            binding.llDogFalse.setVisibility(View.GONE);
            binding.llDogTrue.setVisibility(View.VISIBLE);
            setVisible();
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

    private void handleResponseDog(Dog dog){
        // 급식기가 없을 때
        if(dog.feederId.equals("")){
            binding.clEmptyFeeder.setVisibility(View.VISIBLE);
            binding.clExistFeeder.setVisibility(View.GONE);
        }
        // 급식기가 있을 때
        else{
            binding.clEmptyFeeder.setVisibility(View.GONE);
            binding.clExistFeeder.setVisibility(View.VISIBLE);
            binding.tvFeederId.setText(dog.feederId);
        }
        // 배변판이 없을 때
        if(dog.toiletId.equals("")){
            binding.clEmptyToilet.setVisibility(View.VISIBLE);
            binding.clExistToilet.setVisibility(View.GONE);
        }
        // 배변판이 있을 때
        else{
            binding.clEmptyToilet.setVisibility(View.GONE);
            binding.clExistToilet.setVisibility(View.VISIBLE);
            binding.tvToiletId.setText(dog.toiletId);
        }
    }
    private void handleError(Throwable error) {
        if (error instanceof HttpException) {
            Gson gson = new GsonBuilder().create();
            try {
                String errorBody = ((HttpException) error).response().errorBody().string();
                Res response = gson.fromJson(errorBody, Res.class);
                Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getContext(), "Network Error :(", Toast.LENGTH_SHORT).show();
            Log.d("TESTPAENG", String.valueOf(error));
        }
    }


    public void setVisible(){
        loadDogInf(selectedDogId);

        ArrayList<String> settingFeeder = new ArrayList<String>(Arrays.asList(getContext().getResources().getStringArray(R.array.device_setting)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, settingFeeder);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerFeeder.setAdapter(adapter);
        binding.spinnerToilet.setAdapter(adapter);
        binding.spinnerFeeder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:

                        break;
                    case 1:
                        /*
                        Dog dog = dbHelper.getDogById(selectedDogId);
                        dbHelper.unregisterFeeder(dog.id);
                        dbHelper.deleteScheduleByFeederId(dog.feederId);
                        Toast.makeText(getContext(), "급식기 해제 완료", Toast.LENGTH_SHORT).show();
                        setExistDevice();
                        */
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        binding.spinnerToilet.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position){
                    case 0:
                        break;
                    case 1:
                        /*
                        dbHelper.unregisterToilet(selectedDogId);
                        Toast.makeText(getContext(), "배변판 해제 완료", Toast.LENGTH_SHORT).show();
                        setExistDevice();
                        */
                        break;
                    default:
                        break;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // (급식기) 기기 추가 버튼
    public void addFeederListener(View view){
        Intent intentFeeder = new Intent(getContext(), AddDeviceActivity.class);
        intentFeeder.putExtra("mode", 1);
        startActivity(intentFeeder);
        //// 이건 안해도 되지 않나
    }
    // (배변판) 기기 추가 버튼
    public void addToiletListener(View view){
        Intent intentToilet = new Intent(getContext(), AddDeviceActivity.class);
        intentToilet.putExtra("mode", 2);
        startActivity(intentToilet);

    }

    // (급식기) 수동 배식
    public void feederFeedListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(getContext());
        customDialogFeed.callFunction(1, "배식할 양을 설정해주세요.", "배식", "취소");
    }
    // (급식기) 자동 배식
    public void feederFeedAutoListener(View view){
        // CustomDialog customDialogFeed = new CustomDialog(getContext());
        // customDialogFeed.callFunction(2, "자동 배식 설정", "설정", "취소", binding.pbFuncFeederAutoFeed);

        startActivity(new Intent(getContext(), AutoFeedActivity.class));


    }
    // (급식기) 카메라
    public void feederCameraListener(View view){
        Toast.makeText(getContext(), "camera", Toast.LENGTH_SHORT).show();

    }

    public void settingToiletListener(View view){
        Toast.makeText(getContext(), "toilet", Toast.LENGTH_SHORT).show();

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
        Toast.makeText(getContext(), String.valueOf(selectedDogId), Toast.LENGTH_SHORT).show();
    }




}
