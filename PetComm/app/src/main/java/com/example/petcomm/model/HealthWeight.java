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

    public int getId() {
        return id;
    }

    public String getToiletId() {
        return toiletId;
    }

    public String getWeight() {
        return weight;
    }

    public String getCreated_at() {
        return created_at;
    }
}
