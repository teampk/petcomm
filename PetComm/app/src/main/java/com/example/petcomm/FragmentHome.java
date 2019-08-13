package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentHomeBinding;
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

public class FragmentHome extends Fragment{

    FragmentHomeBinding binding;
    private ArrayList<String> dogList;
    private ArrayList<Dog> dogDataArrayList;
    private ArrayList<FeedSchedule> feedScheduleList;

    private String signInEmail;
    private int dataMode;
    private int selectedDogId;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private int pictureMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
        final View mView = binding.getRoot();
        binding.setFragmentHome(this);
        mSubscriptions = new CompositeSubscription();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        signInEmail = mSharedPreferences.getString(Constants.EMAIL, "");

        loadDogList();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadDogList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    // for server DB
    private void loadDogList(){
        mSubscriptions.add(NetworkUtil.getRetrofit().getDogsById(signInEmail)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseDog,this::handleError));
    }

    private void handleResponseDog(Dog[] dogs) {

        // 서버 DB 로부터 Dog Array List 에 추가
        dogDataArrayList = new ArrayList<Dog>();
        for(Dog dogitem : dogs){
            if(dogitem != null){
                dogDataArrayList.add(dogitem);
            }
        }

        // 선택을 위한 Spinner 리스트에 추가
        dogList = new ArrayList<String>();
        dogList.add(getString(R.string.tv_unselected_dog));
        for (int i=0;i<dogDataArrayList.size();i++){
            dogList.add(dogDataArrayList.get(i).dogName);
        }

        // Spinner 클릭 리스너
        binding.spinnerDog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setDogProfile(position-1);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                setDogProfile(-1);

            }
        });

        // Spinner 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, dogList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDog.setAdapter(adapter);

        // 화면 이동 시 홈 화면 띄울 모습
        if(dogList.size()==1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
            binding.tvEmptyDog.setText(getText(R.string.tv_empty_dog));
        }else{
            binding.spinnerDog.setSelection(findDogListIndexByDogId(mSharedPreferences.getString(Constants.DOG, "")));
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

    public int findDogListIndexById(int id){
        int index = -1;

        for (int i=0;i<dogDataArrayList.size();i++){
            if(dogDataArrayList.get(i).id == id){
                index = i;
                break;
            }
        }
        return index+1;
    }
    public int findDogListIndexByDogId(String dogId){
        int index = -1;
        for (int i=0;i<dogDataArrayList.size();i++) {
            if(dogDataArrayList.get(i).dogId.equals(dogId)){
                index = i;
                break;
            }
        }
        return index+1;
    }

    private void setDogProfile(int index){
        // Spinner에서 선택된 강아지가 없을 때.
        if(index == -1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
            binding.tvEmptyDog.setText(getText(R.string.tv_empty_dog));
            mEditor = mSharedPreferences.edit();
            mEditor.putString(Constants.DOG, "");
            mEditor.apply();
        }
        // Spinner에서 강아지 선택 완료
        else{
            if (dogDataArrayList.get(index).dogBreeds.equals("Bichon Frise")){
                binding.ivProfile.setImageResource(R.drawable.dog_example1);
            }else if (dogDataArrayList.get(index).dogBreeds.equals("Pug")){
                binding.ivProfile.setImageResource(R.drawable.dog_example2);
            }else{
                binding.ivProfile.setImageResource(R.drawable.dog_example3);
            }


            binding.clExistDog.setVisibility(View.VISIBLE);
            binding.clEmptyDog.setVisibility(View.GONE);
            binding.tvName.setText(dogDataArrayList.get(index).dogName);
            // 등록된 급식기가 없을 때
            if(dogDataArrayList.get(index).feederId.equals("")) {
                binding.tvPlanEmpty.setVisibility(View.VISIBLE);
                binding.tvPlanRecycler.setVisibility(View.GONE);
                binding.tvDevice.setText(getString(R.string.tv_device_empty));
                binding.tvPlanEmpty.setText("급식기를 등록해주세요.");
                binding.tvRecommend.setText(getString(R.string.tv_recommend_empty));
            }
            // 급식기 등록 완료된 상태
            else{
                binding.tvDevice.setText(dogDataArrayList.get(index).feederId);
                if (mSharedPreferences.getInt(Constants.DATA_MODE, 1) == 1){
                    binding.tvRecommend.setText(getString(R.string.tv_recommend_initial));

                }else if (mSharedPreferences.getInt(Constants.DATA_MODE, 1) == 2){
                    binding.tvRecommend.setText(getString(R.string.tv_recommend_normal));

                }else if (mSharedPreferences.getInt(Constants.DATA_MODE, 1) == 3){
                    binding.tvRecommend.setText(getString(R.string.tv_recommend_fat));

                }else if (mSharedPreferences.getInt(Constants.DATA_MODE, 1) == 4){
                    binding.tvRecommend.setText(getString(R.string.tv_recommend_danger));
                }

                binding.tvRecommend.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v) {
                        switch(mSharedPreferences.getInt(Constants.DATA_MODE, 1)){
                            case 1:
                                // 2로 이동
                                binding.tvRecommend.setText(getString(R.string.tv_recommend_normal));
                                mEditor = mSharedPreferences.edit();
                                mEditor.putInt(Constants.DATA_MODE, 2);
                                mEditor.apply();
                                break;

                            case 2:
                                binding.tvRecommend.setText(getString(R.string.tv_recommend_fat));
                                mEditor = mSharedPreferences.edit();
                                mEditor.putInt(Constants.DATA_MODE, 3);
                                mEditor.apply();
                                break;

                            case 3:
                                binding.tvRecommend.setText(getString(R.string.tv_recommend_danger));
                                mEditor = mSharedPreferences.edit();
                                mEditor.putInt(Constants.DATA_MODE, 4);
                                mEditor.apply();
                                break;

                            case 4:
                                binding.tvRecommend.setText(getString(R.string.tv_recommend_initial));
                                mEditor = mSharedPreferences.edit();
                                mEditor.putInt(Constants.DATA_MODE, 1);
                                mEditor.apply();
                                break;
                        }
                    }
                });

                // 배식 계획 불러오기
                loadSchedule(dogDataArrayList.get(index).feederId);
            }

            // 등록된 배변판이 없을 때
            if(dogDataArrayList.get(index).toiletId.equals("")){
                binding.tvDevice2.setText(getString(R.string.tv_device_empty));
            }else{
                binding.tvDevice2.setText(dogDataArrayList.get(index).toiletId);
            }
            selectedDogId = dogDataArrayList.get(index).id;
            mEditor = mSharedPreferences.edit();
            mEditor.putString(Constants.DOG, dogDataArrayList.get(index).dogId);
            mEditor.apply();
        }

    }
    // >>>>>>>>>>>>>>>>>>>>>>>>> 스케줄 불러오기
    private void loadSchedule(String feederId){
        mSubscriptions.add(NetworkUtil.getRetrofit().getFeedSchedule(feederId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponseFeeder,this::handleError));
    }
    private void handleResponseFeeder(FeedSchedule[] feedSchedules){

        if(feedSchedules.length == 0){
            binding.tvPlanEmpty.setVisibility(View.VISIBLE);
            binding.tvPlanEmpty.setText("등록된 배식 계획이 없습니다.");
            binding.tvPlanRecycler.setVisibility(View.GONE);
        }else{
            feedScheduleList = new ArrayList<>();
            binding.tvPlanEmpty.setVisibility(View.GONE);
            binding.tvPlanRecycler.setVisibility(View.VISIBLE);
            for(FeedSchedule sitem : feedSchedules){
                if(sitem != null){
                    feedScheduleList.add(sitem);
                }
            }
            binding.tvPlanRecycler.setHasFixedSize(true);
            binding.tvPlanRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
            binding.tvPlanRecycler.scrollToPosition(0);

            RecyclerCustomAdapter mAdapter = new RecyclerCustomAdapter(getContext(), listSorting(feedScheduleList), 2);
            binding.tvPlanRecycler.setAdapter(mAdapter);
            binding.tvPlanRecycler.setItemAnimator(new DefaultItemAnimator());
        }
    }

    public ArrayList<FeedSchedule> listSorting(ArrayList<FeedSchedule> inputAl){
        ArrayList<FeedSchedule> outputAl = new ArrayList<>();
        for (int i=0; i<inputAl.size();i++){
            outputAl.add(inputAl.get(i));
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

    // >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

    public void addDogListener(View view){
        startActivity(new Intent(getContext(), AddDogActivity.class));
    }

    public void dogProfileListener(View view){
        Intent intent = new Intent(getContext(), DogProfileActivity.class);
        intent.putExtra("dogId", mSharedPreferences.getString(Constants.DOG, ""));
        intent.putExtra("pictureMode", pictureMode);
        startActivity(intent);

    }

    public void dataListener(View view){
        startActivity(new Intent(getContext(), DataActivity.class));
    }


    public void testListener(View view){

    }

}
