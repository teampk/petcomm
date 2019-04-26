package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.petcomm.databinding.FragmentHomeBinding;
import com.example.petcomm.model.Dog;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentHome extends Fragment implements AdapterView.OnItemSelectedListener{

    FragmentHomeBinding binding;
    private DBHelper dbHelper;
    private ArrayList<String> dogList;
    private ArrayList<Dog> dogData;

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
        setData();
        setVisible();

        return mView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setData();
        setVisible();

    }
    private void setData(){
        dbHelper = new DBHelper(getContext(), "PetComm.db", null, 1);
        dogList = new ArrayList<>();
        dogList.add("강아지를 선택해주세요");
        dogData = new ArrayList<>();
        dogData = dbHelper.getDogData();
        for (int i=0;i<dogData.size();i++){
            dogList.add(dogData.get(i).name);
        }




    }

    private void setVisible(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, dogList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDog.setAdapter(adapter);

        if(dogList.size()==1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
        }else{
            binding.clEmptyDog.setVisibility(View.GONE);
            binding.clExistDog.setVisibility(View.VISIBLE);

        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l){

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        setDogProfile("");
    }

    private void setDogProfile(String name){
        if(name.equals("")){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
        }
    }
    public void addDogListener(View view){
        startActivity(new Intent(getContext(), SignUpDogActivity.class));
    }
    public void testListener(View view){
        Toast.makeText(getActivity(), String.valueOf(dogList.size()), Toast.LENGTH_SHORT).show();
    }

}
