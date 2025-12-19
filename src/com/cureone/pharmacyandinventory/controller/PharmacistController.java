package com.cureone.pharmacyandinventory.controller;

import com.cureone.pharmacyandinventory.model.Invoice;
import com.cureone.pharmacyandinventory.service.BillingService;

import java.util.List;
import java.util.Scanner;

/**
 * PharmacistController — top-level pharmacist menu that routes
 * to MedicineController, InventoryController and Billing-related operations.
 *
 * NOTE: Session removed. Main must ensure only pharmacist role calls this controller.
 */
public class PharmacistController {

    private final MedicineController medicineController;
    private final InventoryController inventoryController;
    private final BillingService billingService;
    private final Scanner scanner = new Scanner(System.in);

    public PharmacistController(MedicineController medicineController,
                                InventoryController inventoryController,
                                BillingService billingService) {
        this.medicineController = medicineController;
        this.inventoryController = inventoryController;
        this.billingService = billingService;
    }

    public void startStaffMenu() {
        // Session-based check removed. Main must ensure role gating with AuthService.
        while (true) {
            System.out.println("\n=== Pharmacist (Staff) Menu ===");
            System.out.println("1. Inventory options");
            System.out.println("2. Medicine options");
            System.out.println("3. Billing / Invoices");
            System.out.println("0. Back to main");
            System.out.print("Choose: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> inventoryController.startMenu();
                case "2" -> medicineController.startMenu();
                case "3" -> billingMenu();
                case "0" -> { return; }
                default -> System.out.println("Invalid option. Choose 1,2,3 or 0.");
            }
        }
    }

    private void billingMenu() {
        while (true) {
            System.out.println("\n--- Billing / Invoices ---");
            System.out.println("1. View all invoices");
            System.out.println("2. View invoice by id");
            System.out.println("0. Back");
            System.out.print("Choose: ");
            String ch = scanner.nextLine().trim();

            switch (ch) {
                case "1" -> viewAllInvoices();
                case "2" -> viewInvoiceById();
                case "0" -> { return; }
                default -> System.out.println("Invalid option");
            }
        }
    }

    private void viewAllInvoices() {
        List<Invoice> list = billingService.getAllInvoices();
        if (list == null || list.isEmpty()) {
            System.out.println("(no invoices)");
            return;
        }
        list.forEach(System.out::println);
    }

    private void viewInvoiceById() {
        System.out.print("Enter invoice id (numeric) or invoice number (e.g. INV-...): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) { System.out.println("Invalid input"); return; }

        try {
            int id = Integer.parseInt(input);
            var inv = billingService.getInvoiceById(id);
            if (inv == null) System.out.println("Invoice not found");
            else System.out.println(inv);
            return;
        } catch (NumberFormatException ignored) { }

        var invByNumber = billingService.getInvoiceByNumber(input);
        if (invByNumber == null) System.out.println("Invoice not found");
        else System.out.println(invByNumber);
    }
}
