package com.example.petcomm.model;

public class HealthWeight {
    private int id;
    private String toiletId;
    private String weight;
    private String created_at;


    public HealthWeight(int id, String toiletId, String weight, String created_at){
        this.id = id;
        this.toiletId = toiletId;
        this.weight = weight;
        this.created_at = created_at;

    }

    public int getmId(){
        return this.id;
    }

    public String getmFeederId() {
        return this.toiletId;
    }

    public String getmFeedAmount() {
        return this.weight;
    }

    public String getCreated_at() {
        return this.created_at;
    }

}
