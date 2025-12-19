package com.cureone;

import com.cureone.auth.*;
import com.cureone.appointmentsandscheduling.repository.*;
import com.cureone.appointmentsandscheduling.service.*;
import com.cureone.patientsandrecords.repository.*;
import com.cureone.patientsandrecords.service.*;
import com.cureone.pharmacyandinventory.repository.*;
import com.cureone.pharmacyandinventory.service.*;
import com.cureone.gui.*;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {

        // ---------- REPOSITORIES ----------
        AppointmentRepository appointmentRepo = new JdbcAppointmentRepository();
        DoctorRepository doctorRepo = new JdbcDoctorRepository();
        InterfacePatientRepository patientRepo = new JdbcPatientRepository();
        MedicineRepository medicineRepo = new JdbcMedicineRepository();
        InventoryRepository inventoryRepo = new JdbcInventoryRepository();
        CategoryRepository categoryRepo = new JdbcCategoryRepository();
        BillingRepository billingRepo = new JdbcBillingRepository();
        UserRepository userRepo = new JdbcUserRepository();

        // ---------- SERVICES ----------
        AppointmentService appointmentService = new AppointmentService(appointmentRepo);
        DoctorService doctorService = new DoctorService(doctorRepo);
        PatientService patientService = new PatientService(patientRepo);
        MedicineService medicineService = new MedicineServiceImpl(medicineRepo);
        InventoryServices inventoryService =
                new InventoryServiceImpl(inventoryRepo, medicineRepo);
        BillingService billingService =
                new BillingServiceImpl(inventoryRepo, medicineRepo, billingRepo);
        AuthService authService = new AuthService(userRepo);
        GUIContext.categoryService =
                new CategoryServiceImpl(categoryRepo);

        GUIContext.medicineService =
                new MedicineServiceImpl(medicineRepo);

        GUIContext.inventoryService =
                new InventoryServiceImpl(inventoryRepo, medicineRepo);

        GUIContext.billingService =
                new BillingServiceImpl(inventoryRepo, medicineRepo, billingRepo);


        // ---------- GUI CONTEXT ----------
        GUIContext.appointmentService = appointmentService;
        GUIContext.doctorService = doctorService;
        GUIContext.patientService = patientService;
        GUIContext.medicineService = medicineService;
        GUIContext.inventoryService = inventoryService;
        GUIContext.billingService = billingService;
        GUIContext.authService = authService;

        // ---------- START GUI ----------
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
