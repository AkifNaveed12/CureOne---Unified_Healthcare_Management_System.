package com.cureone.pharmacyandinventory.model;

public class Category {

    //category class will store the basic medicine info like its description, name and its id
    //it will also consist of the constructor and getter setters for private data members.....
    private String name;
    private String description;
    private int id;

    public Category() {}
    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getDescription(){
        return this.description;
    }
    public void setDescription(String description){
        this.description = description;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id){
        this.id = id;
    }

    @Override
    public String toString() {
        return " ( 1.id= " +id + ", 2.name= " +name + ", 3.description= " +description + " )";
    }
}
