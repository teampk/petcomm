package com.example.petcomm.model;

public class HealthPoop {
    private int id;
    private String toiletId;
    private String poopAmount;
    private String created_at;


    public HealthPoop(int id, String toiletId, String poopAmount, String created_at){
        this.id = id;
        this.toiletId = toiletId;
        this.poopAmount = poopAmount;
        this.created_at = created_at;

    }

    public int getmId(){
        return this.id;
    }

    public String getmFeederId() {
        return this.toiletId;
    }

    public String getmFeedAmount() {
        return this.poopAmount;
    }

    public String getCreated_at() {
        return this.created_at;
    }

}
