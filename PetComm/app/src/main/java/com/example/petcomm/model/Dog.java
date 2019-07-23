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


    public Dog(){

    }

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

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setBreeds(String breeds) {
        this.breeds = breeds;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFeederId(String feederId) {
        this.feederId = feederId;
    }

    public void setToiletId(String toiletId) {
        this.toiletId = toiletId;
    }
}