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

    public int getId() {
        return id;
    }

    public String getToiletId() {
        return toiletId;
    }

    public String getPoopAmount() {
        return poopAmount;
    }

    public String getCreated_at() {
        return created_at;
    }
}
