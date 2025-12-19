package com.cureone.pharmacyandinventory.controller;

import com.cureone.common.Result;
import com.cureone.pharmacyandinventory.model.Cart;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.BillingService;
import com.cureone.pharmacyandinventory.service.MedicineService;

import java.util.List;
import java.util.Scanner;

public class PharmacyController {

    private final BillingService billingService;
    private final MedicineService medicineService;
    private final Cart cart = new Cart();
    private final Scanner scanner = new Scanner(System.in);

    public PharmacyController(BillingService billingService, MedicineService medicineService) {
        this.billingService = billingService;
        this.medicineService = medicineService;
    }

    // MenuInvoker entry (CUSTOMER)
    public void showMenu() {
        startCustomerMenu();
    }

    public void startCustomerMenu() {
        while (true) {
            System.out.println("\n--- Pharmacy (Customer) ---");
            System.out.println("1. Search medicine by name");
            System.out.println("2. Add medicine to cart");
            System.out.println("3. View cart");
            System.out.println("4. Checkout");
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
                case 1 -> searchByName();
                case 2 -> addToCart();
                case 3 -> viewCart();
                case 4 -> checkout();
                case 0 -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void searchByName() {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();

        List<Medicine> list = medicineService.searchByName(name);
        if (list.isEmpty()) {
            System.out.println("(none)");
            return;
        }
        list.forEach(System.out::println);
    }

    private void addToCart() {
        System.out.print("Enter medicine id: ");
        int id = Integer.parseInt(scanner.nextLine());

        System.out.print("Quantity: ");
        int q = Integer.parseInt(scanner.nextLine());

        Result<String> res = billingService.addToCart(cart, id, q);
        System.out.println(res.getMessage());
    }

    private void viewCart() {
        System.out.println(cart);
    }

    private void checkout() {
        System.out.print("Customer name (optional): ");
        String name = scanner.nextLine();

        Result<?> res = billingService.checkout(cart, name, 0);
        System.out.println(res.getMessage());

        if (res.isSuccess() && res.getData() != null) {
            System.out.println(res.getData());
        }
    }
}
