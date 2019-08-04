package com.example.petcomm.model;

public class FeedSchedule{
    private int id;
    private String feederId;
    private int feedOrder;
    private String feedTime;
    private String feedAmount;


    public FeedSchedule(int id, String feederId, int feedOrder, String feedTime, String feedAmount){
        this.id = id;
        this.feederId = feederId;
        this.feedOrder = feedOrder;
        this.feedTime = feedTime;
        this.feedAmount = feedAmount;

    }

    public int getmId(){
        return this.id;
    }

    public String getFeederId() {
        return this.feederId;
    }
    public int getFeedOrder(){
        return this.feedOrder;
    }
    public String getFeedTime() {
        return this.feedTime;
    }
    public String getFeedAmount() {
        return this.feedAmount;
    }


    public void setFeedOrder(int feedOrder) {
        this.feedOrder = feedOrder;
    }
}
