package com.example.petcomm.model;

public class FeedSchedule{
    public int id;
    public String feederId;
    public String feedTime;
    public String feedAmount;

    public FeedSchedule(int id, String feederId, String feedTime, String feedAmount){
        this.id = id;
        this.feederId = feederId;
        this.feedTime = feedTime;
        this.feedAmount = feedAmount;


    }
}
