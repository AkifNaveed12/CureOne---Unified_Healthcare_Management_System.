import com.cureone.auth.Session;
import com.cureone.pharmacyandinventory.controller.*;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.repository.*;
import com.cureone.pharmacyandinventory.service.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // --- repositories (in-memory)
        MedicineRepository medRepo = new InMemoryMedicineRepository();
        InventoryRepository invRepo = new InMemoryInventoryRepository();
        BillingRepository billRepo = new InMemoryBillingRepository();

        // --- services
        MedicineService medService = new MedicineServiceImpl(medRepo);
        InventoryServices invService = new InventoryServiceImpl(invRepo, medRepo);
        BillingService billingService = new BillingServiceImpl(invRepo, medRepo, billRepo);

        // --- static categories
        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Pain Relief", "Medicines used for pain", 1));
        categories.add(new Category("Antibiotics", "Bacterial treatments", 2));
        categories.add(new Category("Vitamins", "Supplements", 3));

        // --- controllers
        MedicineController medController = new MedicineController(medService, categories);
        InventoryController invController = new InventoryController(invService, medService);
        PharmacyController pharmacyController = new PharmacyController(billingService, medService);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== CureOne CLI ===");
            System.out.println("1. Enter as Customer");
            System.out.println("2. Enter as Pharmacist (staff)");
            System.out.println("0. Exit");
            System.out.print("Choose: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1" -> {
                    System.out.println("Welcome customer!");
                    pharmacyController.startCustomerMenu();
                }
                case "2" -> {
                    System.out.print("Enter staff code (type 'pharm' to proceed): ");
                    String code = sc.nextLine().trim();
                    if ("pharm".equals(code)) {
                        Session session = new Session(Session.Role.PHARMACIST, 1, "pharmacist1");

                        // NEW: create the PharmacistController gateway that delegates to existing controllers
                        PharmacistController staff = new PharmacistController(
                                medController,
                                invController,
                                billingService,
                                session
                        );
                        staff.startStaffMenu();
                    } else {
                        System.out.println("Invalid staff code");
                    }
                }
                case "0" -> { System.out.println("----------------Thanks for Using CureOne Pharmacy-----------------------"); System.exit(0); }
                default -> System.out.println("Invalid");
            }
        }
    }
}
