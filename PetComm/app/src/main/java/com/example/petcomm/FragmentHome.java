package com.example.petcomm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentHomeBinding;
import com.example.petcomm.model.Dog;
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
    private DBHelper dbHelper;
    private ArrayList<String> dogList;
    private ArrayList<Dog> dogDataArrayList;

    private String signInEmail;
    private int selectedDogId;
    private CompositeSubscription mSubscriptions;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

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

        // setDogList();
        loadDogList();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // setDogList();
        loadDogList();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mSubscriptions.unsubscribe();
    }

    // for inner DB
    private void setDogList(){
        dbHelper = new DBHelper(getContext(), "PetComm.db", null, 1);
        dogDataArrayList = new ArrayList<>();
        dogDataArrayList = dbHelper.getDogData();
        dogList = new ArrayList<>();
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
            // Log.d("TESTPAENG", "shared::"+String.valueOf(findDogListIndexById(mSharedPreferences.getInt(Constants.DOG, 0))));
            binding.spinnerDog.setSelection(findDogListIndexById(mSharedPreferences.getInt(Constants.DOG, 0)));
        }

    }
    // for server DB
    private void loadDogList(){
        mSubscriptions.add(NetworkUtil.getRetrofit().loadDogs(signInEmail)
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
            // Log.d("TESTPAENG", "shared::"+String.valueOf(findDogListIndexById(mSharedPreferences.getInt(Constants.DOG, 0))));
            binding.spinnerDog.setSelection(findDogListIndexById(mSharedPreferences.getInt(Constants.DOG, 0)));
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

    public void registerDeviceListener(View view){

    }
    public void testListener(View view){

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

    private void setDogProfile(int id){

        if(id == -1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
            binding.tvEmptyDog.setText(getText(R.string.tv_empty_dog));
            mEditor = mSharedPreferences.edit();
            mEditor.putInt(Constants.DOG, 0);
            mEditor.apply();
        }
        else{
            binding.clEmptyDog.setVisibility(View.GONE);
            binding.clExistDog.setVisibility(View.VISIBLE);
            binding.tvName.setText(dogDataArrayList.get(id).dogName);
            if(dogDataArrayList.get(id).feederId.equals("")){
                binding.tvDevice.setText(getString(R.string.tv_device_empty));
            }else{
                binding.tvDevice.setText(dogDataArrayList.get(id).feederId);
            }

            if(dogDataArrayList.get(id).toiletId.equals("")){
                binding.tvDevice2.setText(getString(R.string.tv_device_empty));
            }else{
                binding.tvDevice2.setText(dogDataArrayList.get(id).toiletId);
            }
            selectedDogId = dogDataArrayList.get(id).id;
            mEditor = mSharedPreferences.edit();
            mEditor.putInt(Constants.DOG, dogDataArrayList.get(id).id);
            mEditor.apply();
        }
    }


    public void addDogListener(View view){
        startActivity(new Intent(getContext(), RegisterDogActivity.class));
    }

    public void dogProfileListener(View view){
        Intent intent = new Intent(getContext(), DogProfileActivity.class);
        intent.putExtra("dogId", mSharedPreferences.getInt(Constants.DOG, -1));
        // Log.d("PETCOMMTEST", String.valueOf(selectedDogId));
        startActivity(intent);

    }

}
