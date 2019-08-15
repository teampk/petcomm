package com.example.petcomm.model;

public class HealthEat {
    private int id;
    private String feederId;
    private String feedAmount;
    private String created_at;


    public HealthEat(int id, String feederId, String feedAmount, String created_at){
        this.id = id;
        this.feederId = feederId;
        this.feedAmount = feedAmount;
        this.created_at = created_at;

    }

    public int getmId(){
        return this.id;
    }

    public String getmFeederId() {
        return this.feederId;
    }

    public String getCreated_at() {
        return this.created_at;
    }

    public String getmFeedAmount() {
        return this.feedAmount;
    }
}
