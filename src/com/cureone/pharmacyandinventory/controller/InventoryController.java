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

    // MenuInvoker entry
    public void showMenu() {
        startMenu();
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n--- Inventory Menu ---");
            System.out.println("1. Add Inventory Item");
            System.out.println("2. List Inventory");
            System.out.println("3. Find Item (inventory id OR medicine id)");
            System.out.println("4. Reduce Stock");
            System.out.println("5. Delete Item");
            System.out.println("6. Low Stock Items");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int ch;
            try {
                ch = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (ch) {
                case 1 -> addItem();
                case 2 -> listAll();
                case 3 -> findByIdOrMedicineId();
                case 4 -> reduceStock();
                case 5 -> deleteItem();
                case 6 -> lowStock();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
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
    }

    private void listAll() {
        List<InventoryItem> list = inventoryService.getAllItems();
        if (list.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        list.forEach(System.out::println);
    }

    private void findByIdOrMedicineId() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());

        InventoryItem item = inventoryService.getItemById(id);
        if (item != null) {
            System.out.println(item);
            return;
        }

        List<InventoryItem> byMed = inventoryService.findByMedicineId(id);
        if (byMed == null || byMed.isEmpty()) {
            System.out.println("Not found");
            return;
        }

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
        System.out.print("Enter inventory item id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Result<Boolean> res = inventoryService.deleteItem(id);
        System.out.println(res.getMessage());
    }

    private void lowStock() {
        System.out.print("Threshold: ");
        int t = Integer.parseInt(scanner.nextLine());

        List<InventoryItem> low = inventoryService.findLowStockItems(t);
        if (low.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        low.forEach(System.out::println);
    }
}
