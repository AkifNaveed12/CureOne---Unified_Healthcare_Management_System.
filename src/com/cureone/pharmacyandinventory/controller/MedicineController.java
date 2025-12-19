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
    private final CategoryController categoryController;
    private final Scanner scanner = new Scanner(System.in);

    public MedicineController(MedicineService service,
                              CategoryController categoryController) {
        this.service = service;
        this.categoryController = categoryController;
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
            System.out.println("0. Back");
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
        List<Category> categories = categoryController.getCategories();

        if (categories.isEmpty()) {
            System.out.println("No categories found. Please add categories first.");
            return;
        }

        System.out.print("Name: ");
        String name = scanner.nextLine();
        System.out.print("Manufacturer: ");
        String manufacturer = scanner.nextLine();
        System.out.print("Price: ");
        double price = Double.parseDouble(scanner.nextLine());
        System.out.print("Quantity: ");
        int qty = Integer.parseInt(scanner.nextLine());

        System.out.println("Choose category id:");
        categories.forEach(c ->
                System.out.println(c.getId() + ". " + c.getName())
        );

        int catId = Integer.parseInt(scanner.nextLine());
        Category chosen = categories.stream()
                .filter(c -> c.getId() == catId)
                .findFirst()
                .orElse(null);

        if (chosen == null) {
            System.out.println("Invalid category.");
            return;
        }

        LocalDate expiry;
        try {
            System.out.print("Expiry date (YYYY-MM-DD): ");
            expiry = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format. Use YYYY-MM-DD");
            return;
        }

        Medicine m = new Medicine(0, name, manufacturer, expiry, price, chosen, qty);
        Result<Medicine> res = service.addMedicine(m);
        System.out.println(res.getMessage());
    }

    private void listAll() {
        List<Medicine> list = service.getAllMedicines();
        if (list.isEmpty()) System.out.println("(no medicines)");
        list.forEach(System.out::println);
    }

    private void findById() {
        System.out.print("Enter id: ");
        int id = Integer.parseInt(scanner.nextLine());
        Medicine m = service.getMedicineById(id);
        System.out.println(m == null ? "Not found" : m);
    }

    private void searchByName() {
        System.out.print("Enter name: ");
        List<Medicine> list = service.searchByName(scanner.nextLine());
        if (list.isEmpty()) System.out.println("(none)");
        list.forEach(System.out::println);
    }

    private void updateMedicine() {
        System.out.print("Enter id to update: ");
        int id = Integer.parseInt(scanner.nextLine());
        Medicine existing = service.getMedicineById(id);
        if (existing == null) return;

        System.out.print("New price: ");
        existing.setPrice(Double.parseDouble(scanner.nextLine()));
        System.out.println(service.updateMedicine(existing).getMessage());
    }

    private void deleteMedicine() {
        System.out.print("Enter id: ");
        System.out.println(service.deleteMedicine(
                Integer.parseInt(scanner.nextLine())).getMessage());
    }

    private void showExpired() {
        List<Medicine> expired = service.getExpiredMedicines();
        if (expired.isEmpty()) System.out.println("(none)");
        expired.forEach(System.out::println);
    }
}
