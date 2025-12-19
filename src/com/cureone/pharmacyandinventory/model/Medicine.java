package com.cureone.pharmacyandinventory.model;
import java.time.LocalDate;
public class Medicine {

    //medicine class consists of the blueprint of a medicine objects all the necessary details
    //and the getter setter +  constructors are present here....
    private int id, quantity;
    private String name, manufacturer;
    private LocalDate expiryDate;

    private double price;
    private Category category;

    public Medicine(){}
    public Medicine(int id, String name, String manufacturer, LocalDate expiryDate, double price, Category category, int quantity) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.expiryDate = expiryDate;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getManufacturer(){
        return manufacturer;
    }
    public void setManufacturer(String manufacturer){
        this.manufacturer = manufacturer;
    }
    public LocalDate getExpiryDate(){
        return expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate){
        this.expiryDate = expiryDate;
    }
    public double getPrice(){
        return price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public Category getCategory(){
        return category;
    }
    public void setCategory(Category category){
        this.category = category;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }

    public String toString(){
        return "( 1.Name=" +name+ ", 2.id=" +id+ ", 3.Category=" +category+ ", 4.Price=" +price+
                ", 5.Quantity=" +quantity+ ", 6.ExpiryDate=" +expiryDate+", 7.Manufacturer=" +manufacturer +" )";
    }
}
