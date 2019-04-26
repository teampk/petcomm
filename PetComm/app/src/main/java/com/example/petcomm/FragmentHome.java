package com.example.petcomm;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class FragmentHome extends Fragment{

    FragmentHomeBinding binding;
    private DBHelper dbHelper;
    private ArrayList<String> dogList;
    private ArrayList<Dog> dogData;
    private int selectedId;
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
        dogList.add(getString(R.string.tv_unselected_dog));
        dogData = new ArrayList<>();
        dogData = dbHelper.getDogData();
        for (int i=0;i<dogData.size();i++){
            dogList.add(dogData.get(i).name);
        }

    }

    private void setVisible(){


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

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_spinner_item, dogList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerDog.setAdapter(adapter);

        // 강아지가 없을 때
        if(dogList.size()==1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
            binding.tvEmptyDog.setText(getText(R.string.tv_empty_dog));
        }else{
            binding.spinnerDog.setSelection(dogList.size()-1);
        }


    }

    private void setDogProfile(int id){
        if(id == -1){
            binding.clEmptyDog.setVisibility(View.VISIBLE);
            binding.clExistDog.setVisibility(View.GONE);
            binding.tvEmptyDog.setText(getText(R.string.tv_unselected_dog));
        }
        else{
            binding.clEmptyDog.setVisibility(View.GONE);
            binding.clExistDog.setVisibility(View.VISIBLE);
            binding.tvName.setText(dogData.get(id).name);
            if(dogData.get(id).feederId.equals("")){
                binding.tvDevice.setText(getString(R.string.tv_device_empty));
            }else{
                binding.tvDevice.setText(dogData.get(id).feederId);
            }


            if(dogData.get(id).toiletId.equals("")){
                binding.tvDevice2.setText(getString(R.string.tv_device_empty));
            }else{
                binding.tvDevice2.setText(dogData.get(id).toiletId);

            }
            selectedId = dogData.get(id).id;
        }
    }


    public void addDogListener(View view){
        startActivity(new Intent(getContext(), SignUpDogActivity.class));
    }

    public void dogProfileListener(View view){
        Intent intent = new Intent(getContext(), DogProfileAcitivty.class);
        intent.putExtra("dogId", selectedId);
        Log.d("PETCOMMTEST", String.valueOf(selectedId));
        startActivity(intent);

    }

}
