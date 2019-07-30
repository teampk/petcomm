package com.example.petcomm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.petcomm.databinding.RecyclerItemAccesspointBinding;

import java.util.ArrayList;
import java.util.Vector;

public class AccessPointAdapter extends RecyclerView.Adapter {

    public ArrayList<String> getSelectedWifi() {
        return selectedWifi;
    }
    private ArrayList<String> selectedWifi;

    private Vector<AccessPoint> accessPoints;
    private Context context;

    AccessPointAdapter(Vector<AccessPoint> accessPoints, Context context){
        this.accessPoints = accessPoints;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder holder;
        RecyclerItemAccesspointBinding binding = RecyclerItemAccesspointBinding.inflate(LayoutInflater.from(context), parent, false);
        holder = new AccessPointHolder(binding);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        AccessPointHolder accessPointHolder = (AccessPointHolder)holder;
        final RecyclerItemAccesspointBinding binding = accessPointHolder.binding;
        binding.cardView.setRadius(20.0f);
        final String ssid = accessPoints.get(position).getSsid();
        final String bSsid = accessPoints.get(position).getBssid();
        final String rssi = accessPoints.get(position).getRssi();
        binding.ssidTextView.setText(ssid);
        selectedWifi = new ArrayList<>();
        binding.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PAENGClicked", ssid+"/"+bSsid+"/"+rssi);
                selectedWifi.add(ssid);
                selectedWifi.add(bSsid);
                selectedWifi.add(rssi);
            }
        });
    }

    @Override
    public int getItemCount() {
        return accessPoints.size();
    }

    private class AccessPointHolder extends RecyclerView.ViewHolder{
        RecyclerItemAccesspointBinding binding;

        AccessPointHolder(RecyclerItemAccesspointBinding binding){
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
