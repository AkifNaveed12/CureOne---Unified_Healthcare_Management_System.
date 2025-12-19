//package com.cureone;
//
//import com.cureone.auth.AuthService;
//import com.cureone.auth.JdbcUserRepository;
//import com.cureone.auth.UserRepository;
//
//import com.cureone.appointmentsandscheduling.controller.AppointmentController;
//import com.cureone.appointmentsandscheduling.controller.DoctorController;
//import com.cureone.appointmentsandscheduling.repository.*;
//import com.cureone.appointmentsandscheduling.service.AppointmentService;
//import com.cureone.appointmentsandscheduling.service.DoctorService;
//
//import com.cureone.pharmacyandinventory.controller.*;
//import com.cureone.pharmacyandinventory.repository.*;
//import com.cureone.pharmacyandinventory.service.*;
//import com.cureone.pharmacyandinventory.controller.PharmacyController;
//
//import com.cureone.patientsandrecords.controller.PatientAdminController;
//import com.cureone.patientsandrecords.repository.*;
//import com.cureone.patientsandrecords.service.PatientService;
//
//import java.util.Scanner;
//
//public class Main {
//
//    public static void main(String[] args) {
//
//        Scanner scanner = new Scanner(System.in);
//
//        // -------------------- REPOSITORIES --------------------
//        MedicineRepository medicineRepo = new JdbcMedicineRepository();
//        InventoryRepository inventoryRepo = new JdbcInventoryRepository();
//        AppointmentRepository appointmentRepo = new JdbcAppointmentRepository();
//        DoctorRepository doctorRepo = new JdbcDoctorRepository();
//        InterfacePatientRepository patientRepo = new JdbcPatientRepository();
//        CategoryRepository categoryRepo = new JdbcCategoryRepository();
//        BillingRepository billingRepo = new JdbcBillingRepository();
//
//
//        // -------------------- SERVICES --------------------
//        MedicineService medicineService = new MedicineServiceImpl(medicineRepo);
//        InventoryServices inventoryService =
//                new InventoryServiceImpl(inventoryRepo, medicineRepo);
//
//        DoctorService doctorService = new DoctorService(doctorRepo);
//
//        AppointmentService appointmentService =
//                new AppointmentService(appointmentRepo);
//
//        PatientService patientService =
//                new PatientService(patientRepo);
//        CategoryService categoryService = new CategoryServiceImpl(categoryRepo);
//        BillingService billingService =
//                new BillingServiceImpl(inventoryRepo, medicineRepo, billingRepo);
//
//
//        // -------------------- CONTROLLERS --------------------
//        CategoryController categoryController = new CategoryController(categoryService);
//
//        MedicineController medicineController =
//                new MedicineController(medicineService, categoryController);
//
//        InventoryController inventoryController =
//                new InventoryController(inventoryService, medicineService);
//
//        DoctorController doctorController =
//                new DoctorController(doctorService);
//
//        AppointmentController appointmentController =
//                new AppointmentController(
//                        appointmentService,
//                        appointmentRepo,
//                        doctorRepo
//                );
//
//        PatientAdminController patientAdminController =
//                new PatientAdminController(patientService, appointmentService);
//
//        BillingController billingController =
//                new BillingController(billingService);
//        PharmacyController PharmacyController = new PharmacyController(billingService,medicineService);
//
//
//
//        // -------------------- AUTH --------------------
//        UserRepository userRepo = new JdbcUserRepository();
//        AuthService auth = new AuthService(userRepo);
//
//
//        // -------------------- GUI START --------------------
//        com.cureone.gui.GUIContext.patientService = patientService;
//        com.cureone.gui.GUIContext.appointmentService = appointmentService;
//        com.cureone.gui.GUIContext.doctorService = doctorService;
//        com.cureone.gui.GUIContext.medicineService = medicineService;
//        com.cureone.gui.GUIContext.inventoryService = inventoryService;
//        com.cureone.gui.GUIContext.billingService = billingService;
//
//        javax.swing.SwingUtilities.invokeLater(() -> {
//            new com.cureone.gui.LoginFrame(auth).setVisible(true);
//        });
//
//
//
////        while (true) {
////        System.out.println("1. Sign Up");
////        System.out.println("2. Login");
////        String choice = scanner.nextLine();
////
////        if ("1".equals(choice)) {
////            System.out.print("Username: ");
////            String u = scanner.nextLine();
////
////            System.out.print("Password: ");
////            String p = scanner.nextLine();
////
////            System.out.print("Role (PATIENT/DOCTOR/PHARMACIST): ");
////            String r = scanner.nextLine();
////
////            auth.signup(u, p, r);
////            System.out.println("Signup successful. Please login.");
////        }
////
////        if ("2".equals(choice)) {
////
////            while (true) {
////                System.out.println("\n=== CureOne CLI ===");
////                System.out.println("1. Enter Hospital");
////                System.out.println("2. Enter Pharmacy");
////                System.out.println("0. Exit");
////                System.out.print("Choose: ");
////
////                String top = scanner.nextLine().trim();
////                if ("0".equals(top)) break;
////
////                // ================= HOSPITAL =================
////                if ("1".equals(top)) {
////
////                    System.out.print("Username: ");
////                    String username = scanner.nextLine();
////
////                    System.out.print("Password: ");
////                    String password = scanner.nextLine();
////
////                    User loggedInUser = auth.login(username, password);
////
////                    if (loggedInUser == null) {
////                        System.out.println("Invalid credentials.");
////                        continue;
////                    }
////
////                    String role = loggedInUser.getRole();
////
////                    if ("ADMIN".equalsIgnoreCase(role)) {
////
////                        while (true) {
////                            System.out.println("\n--- Hospital Admin Menu ---");
////                            System.out.println("1. Doctor Management");
////                            System.out.println("2. Patient Management");
////                            System.out.println("3. Appointment Management");
////                            System.out.println("0. Back");
////                            System.out.print("Choose: ");
////
////                            String h = scanner.nextLine();
////                            if ("0".equals(h)) break;
////
////                            if ("1".equals(h)) {
////                                doctorController.showMenu();
////                            } else if ("2".equals(h)) {
////                                MenuInvoker.invokeMenu(patientAdminController);
////                            } else if ("3".equals(h)) {
////                                MenuInvoker.invokeMenu(appointmentController);
////                            } else {
////                                System.out.println("Invalid option");
////                            }
////                        }
////
////                    }
////
////
////                    else if ("DOCTOR".equalsIgnoreCase(role)) {
////                        DoctorSelfController doctorSelf =
////                                new DoctorSelfController(
////                                        appointmentService,
////                                        doctorService,
////                                        loggedInUser.getId()
////                                );
////
////                        MenuInvoker.invokeMenu(doctorSelf);
////
////                    } else {
////                        // PATIENT
////                        PatientSelfController self =
////                                new PatientSelfController(patientService, appointmentService, loggedInUser.getId());
////                        MenuInvoker.invokeMenu(self);
////                    }
////                }
////
////                // ================= PHARMACY =================
////                else if ("2".equals(top)) {
////
////                    System.out.print("Username (or press Enter for customer): ");
////                    String username = scanner.nextLine();
////
////                    User loggedInUser = null;
////                    String role = "CUSTOMER";
////
////                    if (!username.isEmpty()) {
////                        System.out.print("Password: ");
////                        String password = scanner.nextLine();
////
////                        loggedInUser = auth.login(username, password);
////
////                        if (loggedInUser == null) {
////                            System.out.println("Invalid credentials.");
////                            continue;
////                        }
////                        role = loggedInUser.getRole();
////                    }
////
////                    if ("ADMIN".equalsIgnoreCase(role) || "PHARMACIST".equalsIgnoreCase(role)) {
////
////                        while (true) {
////                            System.out.println("\n--- Pharmacy Management Menu ---");
////                            System.out.println("1. Category options");
////                            System.out.println("2. Medicine options");
////                            System.out.println("3. Inventory options");
////                            System.out.println("4. Billing options");
////                            System.out.println("0. Back");
////                            System.out.print("Choose: ");
////
////                            String c = scanner.nextLine();
////                            if ("0".equals(c)) break;
////
////                            if ("1".equals(c)) {
////                                MenuInvoker.invokeMenu(categoryController);
////                            } else if ("2".equals(c)) {
////                                MenuInvoker.invokeMenu(medicineController);
////                            } else if ("3".equals(c)) {
////                                MenuInvoker.invokeMenu(inventoryController);
////                            } else if ("4".equals(c)) {
////                                MenuInvoker.invokeMenu(billingController);
////                                // BillingController
////                            } else {
////                                System.out.println("Invalid choice");
////                            }
////                        }
////
////                    } else {
////                        // CUSTOMER
////                        System.out.println("Customer pharmacy menu:");
////                        MenuInvoker.invokeMenu(PharmacyController);
////                    }
////                }
////
////
////                }
////            }
////            System.out.println("Exiting CureOne. Bye!");
////        }
//    }
//}