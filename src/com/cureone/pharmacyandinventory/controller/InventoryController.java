package com.cureone.pharmacyandinventory.controller;



import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.repository.*;

import java.time.LocalDate;
import java.util.List;

public class InventoryController {

    public static void main(String[] args) {

        // ---------- REPOSITORIES ----------
        MedicineRepository medicineRepo = new InMemoryMedicineRepository();
        InventoryRepository inventoryRepo = new InMemoryInventoryRepository();

        System.out.println("-----------------------------------------------------");
        System.out.println("        TESTING PHARMACY & INVENTORY MODULE");
        System.out.println("-----------------------------------------------------");

        // ---------- CATEGORY ----------
        Category cat1 = new Category("Pain Relief", "Medicines used for relief from body pain", 1);
        Category cat2 = new Category("Antibiotics", "Medicines used to treat bacterial infections", 2);

        System.out.println("\nCreated Categories:");
        System.out.println(cat1);
        System.out.println(cat2);

        // ---------- MEDICINE ----------
        Medicine m1 = new Medicine(
                0,
                "Panadol",
                "GSK",
                LocalDate.of(2025 , 6,30),50.6, cat1,15
        );

        Medicine m2 = new Medicine(
                0, "Augmentin", "SKB", LocalDate.of(2023,12,10),
                100.1, cat2, 50
        );

        medicineRepo.save(m1);
        medicineRepo.save(m2);

        System.out.println("\nAfter Saving Medicines:");
        print(medicineRepo.findAll());

        // ---------- FIND BY ID ----------
        System.out.println("\nFind Medicine By ID 1:");
        System.out.println(medicineRepo.findById(1));

        // ---------- EXPIRED MEDICINES ----------
        System.out.println("\nExpired Medicines:");
        print(medicineRepo.findExpiredMedicines());

        // ---------- UPDATE ----------
        System.out.println("\nUpdating Medicine 1...");
        m1.setManufacturer("Haleon");
        medicineRepo.update(m1);
        System.out.println(medicineRepo.findById(m1.getId()));

        // ---------- INVENTORY ITEMS ----------
        InventoryItem item1 = new InventoryItem(
                0, 50, 7, m1, LocalDate.of(2025,8,12),
                "Shelf A-7 Row No # 03",50.6
        );

        InventoryItem item2 = new InventoryItem(
                0,50,5,m2,LocalDate.of(2023,12,10),
                "Shelf B-10 Row No # 03",100.1
        );

        inventoryRepo.save(item1);
        inventoryRepo.save(item2);

        System.out.println("\nInventory Items After Saving:");
        print(inventoryRepo.findAll());

        // ---------- DELETE ----------
        System.out.println("\nDeleting Inventory Item ID 1...");
        inventoryRepo.delete(1);
        print(inventoryRepo.findAll());

        System.out.println("\nAll tests executed successfully!");
    }

    private static <T> void print(List<T> list) {
        if (list.isEmpty()) {
            System.out.println("  (empty)");
        }
        for (T obj : list) {
            System.out.println("  " + obj);
        }
    }
}

