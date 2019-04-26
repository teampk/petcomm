package com.example.petcomm.model;

public class Dog {
    public int id;
    public String name;
    public String gender;
    public String species;
    public String birth;
    public String weight;

    public Dog(int id, String name, String gender, String species, String birth, String weight){
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.species = species;
        this.birth = birth;
        this.weight = weight;
    }
}
