package com.example.petcomm.model;

public class FeedSchedule{
    private int mId;
    private String mFeederId;
    private String mFeedTime;
    private String mFeedAmount;


    public FeedSchedule(int id, String feederId, String feedTime, String feedAmount){
        this.mId = id;
        this.mFeederId = feederId;
        this.mFeedTime = feedTime;
        this.mFeedAmount = feedAmount;

    }

    public int getmId(){
        return this.mId;
    }

    public String getmFeederId() {
        return this.mFeederId;
    }
    public String getmFeedTime() {
        return this.mFeedTime;
    }
    public String getmFeedAmount() {
        return this.mFeedAmount;
    }
}
