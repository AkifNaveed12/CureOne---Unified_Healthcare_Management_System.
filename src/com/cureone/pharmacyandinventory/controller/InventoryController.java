package com.cureone.pharmacyandinventory.controller;

import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.model.Medicine;

import java.time.LocalDate;

public class InventoryController {
    public static void main(String[] args) {
        Category category = new Category("Anti-Biotic", "It is an Anti-biotic medicine",1 );
        Medicine medicine = new Medicine(112,"Hexaperamol", "Amc-Medical", "13/5/2025", 200.16, category, 30 );
        InventoryItem inventoryItem = new InventoryItem(2, 20, 5, medicine, LocalDate.of(2025,5,13), "Shelf A6, Column 5", 200.16);

        System.out.println(medicine.toString());
        System.out.println(category.toString());
        System.out.println(inventoryItem.toString());
    }
}
