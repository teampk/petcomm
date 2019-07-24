package com.example.petcomm.model;

import java.io.Serializable;

public class Dog implements Serializable {
    public int id;
    public String dogId;
    public String dogName;
    public String dogGender;
    public String dogBreeds;
    public String dogBirth;
    public String dogWeight;
    public String userEmail;
    public String feederId;
    public String toiletId;


    public Dog(){

    }

    public Dog(int id, String name, String gender, String breeds, String birth, String weight, String email, String feederId, String toiletId){
        this.id = id;
        this.dogName = name;
        this.dogGender = gender;
        this.dogBreeds = breeds;
        this.dogBirth = birth;
        this.dogWeight = weight;
        this.userEmail = email;
        this.feederId = feederId;
        this.toiletId = toiletId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDogId(String dogId) {
        this.dogId = dogId;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public void setDogGender(String dogGender) {
        this.dogGender = dogGender;
    }

    public void setDogBreeds(String dogBreeds) {
        this.dogBreeds = dogBreeds;
    }

    public void setDogBirth(String dogBirth) {
        this.dogBirth = dogBirth;
    }

    public void setDogWeight(String dogWeight) {
        this.dogWeight = dogWeight;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public void setFeederId(String feederId) {
        this.feederId = feederId;
    }

    public void setToiletId(String toiletId) {
        this.toiletId = toiletId;
    }
}