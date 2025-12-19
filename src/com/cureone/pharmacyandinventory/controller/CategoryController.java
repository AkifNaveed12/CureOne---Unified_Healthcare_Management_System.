package com.cureone.pharmacyandinventory.controller;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.service.CategoryService;

import java.util.List;
import java.util.Scanner;

public class CategoryController {

    private final CategoryService service;
    private final Scanner scanner = new Scanner(System.in);

    public CategoryController(CategoryService service) {
        this.service = service;
    }

    // MenuInvoker entry
    public void showMenu() {
        startMenu();
    }

    public void startMenu() {

        while (true) {
            System.out.println("\n--- Category Menu ---");
            System.out.println("1. Add Category");
            System.out.println("2. List Categories");
            System.out.println("3. Find Category by ID");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (Exception e) {
                System.out.println("Invalid input");
                continue;
            }

            switch (choice) {
                case 1 -> addCategory();
                case 2 -> listCategories();
                case 3 -> findById();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    // ---------------- ADD ----------------
    private void addCategory() {

        System.out.print("Name: ");
        String name = scanner.nextLine();

        System.out.print("Description: ");
        String desc = scanner.nextLine();

        Category c = new Category(name, desc, 0);

        Result<Category> res = service.addCategory(c);
        System.out.println(res.getMessage());
    }

    // ---------------- LIST ----------------
    private void listCategories() {

        List<Category> list = service.getAllCategories();

        if (list.isEmpty()) {
            System.out.println("(no categories)");
            return;
        }

        list.forEach(System.out::println);
    }

    //------------------- Get all Categories -----------------------
    public List<Category> getCategories() {
        List<Category> categories = service.getAllCategories();
        if (categories.isEmpty()) {
            System.out.println("(no categories)");

        }
        return categories;
    }

    // ---------------- FIND ----------------
    private void findById() {

        System.out.print("Enter category id: ");
        int id = Integer.parseInt(scanner.nextLine());

        Category c = service.getCategoryById(id);

        if (c == null)
            System.out.println("Category not found");
        else
            System.out.println(c);
    }
}
