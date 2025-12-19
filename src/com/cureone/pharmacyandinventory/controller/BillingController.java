package com.cureone.pharmacyandinventory.controller;

import com.cureone.pharmacyandinventory.model.Invoice;
import com.cureone.pharmacyandinventory.service.BillingService;

import java.util.List;
import java.util.Scanner;

public class BillingController {

    private final BillingService billingService;
    private final Scanner scanner = new Scanner(System.in);

    public BillingController(BillingService billingService) {
        this.billingService = billingService;
    }

    // MenuInvoker entry
    public void showMenu() {
        while (true) {
            System.out.println("\n======= BILLING / INVOICES =======");
            System.out.println("1. View all invoices");
            System.out.println("2. View invoice by ID");
            System.out.println("3. View invoice by number");
            System.out.println("0. Back");
            System.out.print("Choose: ");

            String ch = scanner.nextLine();

            switch (ch) {
                case "1" -> viewAll();
                case "2" -> viewById();
                case "3" -> viewByNumber();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void viewAll() {
        List<Invoice> list = billingService.getAllInvoices();
        if (list.isEmpty()) {
            System.out.println("(no invoices)");
            return;
        }
        list.forEach(System.out::println);
    }

    private void viewById() {
        System.out.print("Invoice ID: ");
        int id = Integer.parseInt(scanner.nextLine());

        Invoice inv = billingService.getInvoiceById(id);
        if (inv == null) {
            System.out.println("Invoice not found");
        } else {
            System.out.println(inv);
        }
    }

    private void viewByNumber() {
        System.out.print("Invoice Number (INV-...): ");
        String num = scanner.nextLine();

        Invoice inv = billingService.getInvoiceByNumber(num);
        if (inv == null) {
            System.out.println("Invoice not found");
        } else {
            System.out.println(inv);
        }
    }
}
