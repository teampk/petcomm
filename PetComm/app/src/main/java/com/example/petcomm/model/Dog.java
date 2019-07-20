package com.example.petcomm.model;

public class Dog {
    public int id;
    public String name;
    public String gender;
    public String breeds;
    public String birth;
    public String weight;
    public String email;
    public String feederId;
    public String toiletId;

    public Dog(int id, String name, String gender, String breeds, String birth, String weight, String email, String feederId, String toiletId){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.breeds = breeds;
        this.birth = birth;
        this.weight = weight;
        this.email = email;
        this.feederId = feederId;
        this.toiletId = toiletId;
    }
}