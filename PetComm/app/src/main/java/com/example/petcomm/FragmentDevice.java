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

    private void loadDogInf(){
        mSubscriptions.add(NetworkUtil.getRetrofit().getDogByDogId(selectedDogId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDog,this::handleError));
    }

    private void handleResponseDog(Dog dog){
        selectedDog = dog;
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
            Log.d("NetworkERROR", String.valueOf(error));
            Toast.makeText(getContext(), "NETWORK ERROR :(", Toast.LENGTH_SHORT).show();
        }
    }

    public void setVisible(){
        loadDogInf();

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
                        unregisterFeeder();
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
                        unregisterToilet();
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

    private void unregisterFeeder(){
        mSubscriptions.add(NetworkUtil.getRetrofit().unregisterFeeder(selectedDog.dogId, selectedDog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));

    }
    private void unregisterToilet(){
        mSubscriptions.add(NetworkUtil.getRetrofit().unregisterToilet(selectedDog.dogId, selectedDog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError));
    }
    private void handleResponse(Res response){
        //Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "기기 해제 완료", Toast.LENGTH_SHORT).show();
        // 해당 기기의 배식 스케줄도 모두 삭제
        setFeedScheduleList(selectedDog);
        loadDogInf();
    }
    private void setFeedScheduleList(Dog dog){
        mSubscriptions.add(NetworkUtil.getRetrofit().removeSchedule(dog)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseRemove,this::handleError));
    }
    private void handleResponseRemove(Res response) {
        // Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();

    }

    // (급식기) 기기 추가 버튼
    public void addFeederListener(View view){
        Intent intentFeeder = new Intent(getContext(), AddDeviceActivity.class);
        intentFeeder.putExtra("mode", 1);
        intentFeeder.putExtra("dog", selectedDog);
        startActivity(intentFeeder);
    }
    // (배변판) 기기 추가 버튼
    public void addToiletListener(View view){
        Intent intentToilet = new Intent(getContext(), AddDeviceActivity.class);
        intentToilet.putExtra("mode", 2);
        intentToilet.putExtra("dog", selectedDog);
        startActivity(intentToilet);
    }

    // (급식기) 수동 배식
    public void feederFeedListener(View view){
        CustomDialog customDialogFeed = new CustomDialog(getContext());
        customDialogFeed.callFunction(1, "배식할 양을 설정해주세요.", "배식", "취소");
        customDialogFeed.setDialoglistener(new CustomDialog.CustomDialogListener() {
            @Override
            public void onPositiveClicked(String feedAmount, String a) {


                Toast.makeText(getContext(), feedAmount+"g 배식되었습니다.", Toast.LENGTH_SHORT).show();
                FeedSchedule feedManually = new FeedSchedule(0, selectedDog.feederId, "99:99", feedAmount);

                feedManually(feedManually);


            }

            @Override
            public void onNegativeClicked() {

            }
        });
    }
    private void feedManually(FeedSchedule feedManually){
        mSubscriptions.add(NetworkUtil.getRetrofit().feedManually(feedManually)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleFeedResponse,this::handleError));
    }

    private void handleFeedResponse(Res response){

    }

    // (급식기) 자동 배식
    public void feederFeedAutoListener(View view){
        Intent intentAuto = new Intent(getContext(), AutoFeedActivity.class);
        intentAuto.putExtra("dog", selectedDog);
        startActivity(intentAuto);
    }
    // (급식기) 카메라
    public void feederCameraListener(View view){
        Toast.makeText(getContext(), "camera", Toast.LENGTH_SHORT).show();

    }

    public void settingToiletListener(View view){
        Toast.makeText(getContext(), "toilet", Toast.LENGTH_SHORT).show();

    }




    public void testListener(View view){

    }




}
