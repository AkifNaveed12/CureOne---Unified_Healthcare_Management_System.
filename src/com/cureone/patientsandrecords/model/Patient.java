package com.cureone.patientsandrecords.model;

//Made By Hamza Ali FA24-BSE-124

public class Patient {

    private int id;
    private String name;
    private int age;
    private String gender;
    private String country;
    private String contact;
    private String diseaseDescription;

    public Patient() {}

    public Patient(int id, String name, int age, String gender,
                   String country, String contact,
                   String diseaseDescription) {

        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.contact = contact;
        this.diseaseDescription = diseaseDescription;
    }

    public int getId(){return id;}
    public void setId(int id){this.id = id;}

    public String getName(){return name;}
    public void setName(String name){this.name = name;}

    public int getAge(){return age;}
    public void setAge(int age){this.age = age;}

    public String getGender(){return gender;}
    public void setGender(String gender){this.gender = gender;}

    public String getCountry(){return country;}
    public void setCountry(String country){this.country = country;}

    public String getContact(){return contact;}
    public void setContact(String contact){this.contact = contact;}

    public String getDiseaseDescription(){return diseaseDescription;}
    public void setDiseaseDescription(String diseaseDescription){this.diseaseDescription = diseaseDescription;}

    @Override
    public String toString() {
        return "\nPatient ID: " + id +
                "\nName: " + name +
                "\nAge: " + age +
                "\nGender: " + gender +
                "\nCountry: " + country +
                "\nContact: " + contact +
                "\nDisease: " + diseaseDescription;
    }
}
