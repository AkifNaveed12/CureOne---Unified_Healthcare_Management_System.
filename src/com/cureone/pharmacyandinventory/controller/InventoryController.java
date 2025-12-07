package com.cureone.pharmacyandinventory.controller;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.InventoryServices;
import com.cureone.pharmacyandinventory.service.MedicineService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InventoryController {

    private final InventoryServices inventoryService;
    private final MedicineService medicineService;
    private final Scanner scanner = new Scanner(System.in);

    public InventoryController(InventoryServices inventoryService, MedicineService medicineService) {
        this.inventoryService = inventoryService;
        this.medicineService = medicineService;
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n--- Inventory Menu ---");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. List Inventory");
            System.out.println("3. Find Item by ID (inventory id or medicine id)");
            System.out.println("4. Reduce Stock");
            System.out.println("5. Delete Item");
            System.out.println("6. Low stock items");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            int ch = Integer.parseInt(scanner.nextLine());
            switch (ch) {
                case 1 -> addItem();
                case 2 -> listAll();
                case 3 -> findByIdOrMedicineId();
                case 4 -> reduceStock();
                case 5 -> deleteItem();
                case 6 -> lowStock();
                case 0 -> { return; }
                default -> System.out.println("Invalid");
            }
        }
    }

    private void addItem() {
        System.out.print("Enter medicine id: ");
        int medId = Integer.parseInt(scanner.nextLine());
        Medicine med = medicineService.getMedicineById(medId);
        if (med == null) {
            System.out.println("Medicine not found. Add medicine first.");
            return;
        }

        System.out.print("Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());
        System.out.print("Minimum stock limit: ");
        int min = Integer.parseInt(scanner.nextLine());
        System.out.print("Location: ");
        String loc = scanner.nextLine();
        System.out.print("Expiry date (YYYY-MM-DD): ");
        LocalDate exp = LocalDate.parse(scanner.nextLine());
        System.out.print("Price per unit: ");
        double price = Double.parseDouble(scanner.nextLine());

        InventoryItem item = new InventoryItem(0, qty, min, med, exp, loc, price);
        Result<InventoryItem> res = inventoryService.addInventoryItem(item);
        System.out.println(res.getMessage());

        // --- print the generated inventory item id so pharmacist sees it immediately ---
        if (res.isSuccess()) {
            // since our service returns the saved InventoryItem in Result.data (we didn't earlier),
            // we try to retrieve the item id by searching inventory by medicine and matching by lot properties.
            // But easier & reliable: show the latest list entry(s) or the item object if returned by service.
            // If your InventoryService.addInventoryItem returns the saved item, print it:
            InventoryItem saved = res.getData(); // only works if service set it
            if (saved != null) {
                System.out.println("Inventory Item saved with id: " + saved.getItemId());
            } else {
                // fallback: show full inventory for this medicine (helpful)
                System.out.println("Current inventory for this medicine:");
                List<InventoryItem> list = inventoryService.findByMedicineId(medId);
                list.forEach(System.out::println);
            }
        }
    }

    private void listAll() {
        List<InventoryItem> list = inventoryService.getAllItems();
        if (list.isEmpty()) System.out.println("(none)");
        list.forEach(System.out::println);
    }

    private void findByIdOrMedicineId() {
        System.out.print("Enter id (inventory item id or medicine id): ");
        int id = Integer.parseInt(scanner.nextLine());

        // Try find by inventory item id first
        InventoryItem it = inventoryService.getItemById(id);
        if (it != null) {
            System.out.println(it);
            return;
        }

        // Not an inventory item id — try as medicine id
        List<InventoryItem> byMed = inventoryService.findByMedicineId(id);
        if (byMed == null || byMed.isEmpty()) {
            System.out.println("Not found");
            return;
        }
        System.out.println("Inventory lots for medicine id " + id + ":");
        byMed.forEach(System.out::println);
    }

    private void reduceStock() {
        System.out.print("Enter inventory item id: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Amount to reduce: ");
        int amt = Integer.parseInt(scanner.nextLine());
        Result<Boolean> res = inventoryService.reduceStock(id, amt);
        System.out.println(res.getMessage());
    }

    private void deleteItem() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Result<Boolean> res = inventoryService.deleteItem(id);
        System.out.println(res.getMessage());
    }

    private void lowStock() {
        System.out.print("Threshold (e.g. 10): ");
        int t = Integer.parseInt(scanner.nextLine());
        List<InventoryItem> low = inventoryService.findLowStockItems(t);
        if (low.isEmpty()) System.out.println("(none)");
        low.forEach(System.out::println);
    }
}
