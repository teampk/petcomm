package com.example.petcomm.model;

public class HealthFeed {
    private int id;
    private String feederId;
    private String feedTime;
    private String feedAmount;


    public HealthFeed(int id, String feederId, String feedTime, String feedAmount){
        this.id = id;
        this.feederId = feederId;
        this.feedTime = feedTime;
        this.feedAmount = feedAmount;

    }

    public int getmId(){
        return this.id;
    }

    public String getmFeederId() {
        return this.feederId;
    }
    public String getmFeedTime() {
        return this.feedTime;
    }
    public String getmFeedAmount() {
        return this.feedAmount;
    }
}
