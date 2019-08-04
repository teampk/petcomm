package com.example.petcomm;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.petcomm.model.FeedSchedule;

import java.util.ArrayList;

class MyViewHolderScheduleList extends RecyclerView.ViewHolder {

    TextView itemFeedTime;
    TextView itemFeedAmount;
    LinearLayout itemParent;

    MyViewHolderScheduleList(View itemView) {
        super(itemView);
        itemFeedTime = itemView.findViewById(R.id.tv_recycler_feed_time);
        itemFeedAmount = itemView.findViewById(R.id.tv_recycler_feed_amount);
        itemParent = itemView.findViewById(R.id.tv_recycler_parent);
    }
}

public class RecyclerCustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private ArrayList<FeedSchedule> mDataSet = new ArrayList<>();
    private int mode;

    public RecyclerCustomAdapter(Context context, ArrayList<FeedSchedule> feedScheduleList, int mode){
        this.mContext = context;
        this.mDataSet = feedScheduleList;
        this.mode = mode;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        this.mContext = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        if (mode == 1){
            view = inflater.inflate(R.layout.recycler_item_feed_schedule, viewGroup, false);
        }else if (mode == 2){
            view = inflater.inflate(R.layout.recycler_item_feed_schedule_home, viewGroup, false);
        }else{
            view = inflater.inflate(R.layout.recycler_item_feed_schedule, viewGroup, false);
        }
        return new MyViewHolderScheduleList(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final FeedSchedule selectedData = this.mDataSet.get(position);
        MyViewHolderScheduleList viewHolder = (MyViewHolderScheduleList) holder;

        viewHolder.setIsRecyclable(false);
        viewHolder.itemFeedTime.setText(selectedData.getFeedTime());
        viewHolder.itemFeedAmount.setText(selectedData.getFeedAmount());
        viewHolder.itemParent.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (mode == 1){
                    // Toast.makeText(mContext, selectedData.getmFeedTime(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}
