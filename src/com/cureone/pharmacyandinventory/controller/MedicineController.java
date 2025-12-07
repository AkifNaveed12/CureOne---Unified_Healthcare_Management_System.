package com.cureone.pharmacyandinventory.controller;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.MedicineService;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class MedicineController {

    private final MedicineService service;
    private final List<Category> categories;
    private final Scanner scanner = new Scanner(System.in);

    public MedicineController(MedicineService service, List<Category> categories) {
        this.service = service;
        this.categories = categories;
    }

    public void startMenu() {
        while (true) {
            System.out.println("\n--- Medicine Menu ---");
            System.out.println("1. Add Medicine");
            System.out.println("2. List Medicines");
            System.out.println("3. Find by ID");
            System.out.println("4. Search by Name");
            System.out.println("5. Update Medicine");
            System.out.println("6. Delete Medicine");
            System.out.println("7. Expired Medicines");
            System.out.println("0. Back to main");
            System.out.print("Choose: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1 -> addMedicine();
                case 2 -> listAll();
                case 3 -> findById();
                case 4 -> searchByName();
                case 5 -> updateMedicine();
                case 6 -> deleteMedicine();
                case 7 -> showExpired();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void addMedicine() {
        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        // Pharmacist has to choose from the categories defined here
        System.out.println("Choose category id from list below:");
        for (Category c : categories) {
            System.out.println(c.getId() + ". " + c.getName());
        }
        System.out.print("Category id: ");
        int catId = Integer.parseInt(scanner.nextLine());
        Category chosen = categories.stream().filter(c -> c.getId() == catId).findFirst().orElse(null);
        // logic ****
//        Category chosen = null;
//        for (Category c : categories) {
//            if (c.getId() == catId) { chosen = c; break; }
//        }


        System.out.print("Expiry date (YYYY-MM-DD): ");
        LocalDate expiry = LocalDate.parse(scanner.nextLine());

        Medicine m = new Medicine(0, name, manufacturer, expiry, price, chosen, qty);
        Result<Medicine> res = service.addMedicine(m);
        System.out.println(res.getMessage());
        if (res.isSuccess()) System.out.println(res.getData());
    }

    private void listAll() {
        List<Medicine> list = service.getAllMedicines();
        if (list.isEmpty()) System.out.println("(no medicines)");
        list.forEach(System.out::println);// iterates through the list and prints line by line
    }

    private void findById() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Medicine m = service.getMedicineById(id);
        if (m == null) System.out.println("Not found");
        else System.out.println(m);
    }

    private void searchByName() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        List<Medicine> list = service.searchByName(name);
        if (list.isEmpty()) System.out.println("(none)");
        list.forEach(System.out::println);
    }

    private void updateMedicine() {
        System.out.print("Enter id to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Medicine existing = service.getMedicineById(id);
        if (existing == null) { System.out.println("Not found"); return; }

        System.out.print("New name (" + existing.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.trim().isEmpty()) existing.setName(name);

        System.out.print("New manufacturer (" + existing.getManufacturer() + "): ");
        String man = scanner.nextLine();
        if (!man.trim().isEmpty()) existing.setManufacturer(man);

        System.out.print("New price (" + existing.getPrice() + "): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.trim().isEmpty()) existing.setPrice(Double.parseDouble(priceStr));

        System.out.print("New quantity (" + existing.getQuantity() + "): ");
        String qtyStr = scanner.nextLine();
        if (!qtyStr.trim().isEmpty()) existing.setQuantity(Integer.parseInt(qtyStr));

        System.out.print("New expiry (" + existing.getExpiryDate() + "): ");
        String expStr = scanner.nextLine();
        if (!expStr.trim().isEmpty()) existing.setExpiryDate(LocalDate.parse(expStr));

        Result<Medicine> res = service.updateMedicine(existing);
        System.out.println(res.getMessage());
    }

    private void deleteMedicine() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Result<Boolean> res = service.deleteMedicine(id);
        System.out.println(res.getMessage());
    }

    private void showExpired() {
        List<Medicine> expired = service.getExpiredMedicines();
        if (expired.isEmpty()) System.out.println("(none)");
        expired.forEach(System.out::println);
    }
}
