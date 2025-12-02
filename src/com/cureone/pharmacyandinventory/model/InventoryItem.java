package com.cureone.pharmacyandinventory.model;

import java.time.LocalDate;

public class InventoryItem {

    //this class holds the data memers of the inventory items their getter setters and the  constructors
    private int itemId, quantity, minimumStockLimit;
    private Medicine medicine;
    private LocalDate expiryDate;
    private String location;
    private double price;

    public InventoryItem(){}
    public InventoryItem(int itemId, int quantity, int minimumStockLimit, Medicine medicine, LocalDate expiryDate, String location, double price) {
        this.itemId = itemId;
        this.quantity = quantity;
        this.medicine = medicine;
        this.expiryDate = expiryDate;
        this.location = location;
        this.price = price;
        this.minimumStockLimit = minimumStockLimit;
    }

    public int getItemId(){
        return this.itemId;
    }
    public void setItemId(int itemId){
        this.itemId = itemId;
    }
    public int getQuantity(int quantity){
        return this.quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public Medicine getMedicine(Medicine medicine){
        return this.medicine;
    }
    public void setMedicine(Medicine medicine){
        this.medicine = medicine;
    }
    public LocalDate getExpiryDate(LocalDate expiryDate){
        return this.expiryDate;
    }
    public void setExpiryDate(LocalDate expiryDate){
        this.expiryDate = expiryDate;
    }
    public String getLocation(){
        return this.location;
    }
    public void setLocation(String location){
        this.location = location;
    }
    public double getPrice(){
        return this.price;
    }
    public void setPrice(double price){
        this.price = price;
    }
    public int getMinimumStockLimit(){
        return this.minimumStockLimit;
    }
    public void setMinimumStockLimit(int minimumStockLimit){
        this.minimumStockLimit = minimumStockLimit;
    }

    public String toString(){
        return "Inventory\n1.Name= " +medicine.getName() +"\n2.Quantity= " +quantity
                +"\n3.MinimumStockLimit= " +minimumStockLimit +"\n4.ItemId= " +itemId +"\n5.Price= "+ price +
                "\n6.Location= " +location+"\n7.ExpiryDate= " +expiryDate;
    }
}
