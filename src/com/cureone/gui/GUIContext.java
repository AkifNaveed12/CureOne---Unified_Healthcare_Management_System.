package com.cureone.gui;

import com.cureone.auth.AuthService;
import com.cureone.auth.User;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.appointmentsandscheduling.service.DoctorService;
import com.cureone.patientsandrecords.service.PatientService;
import com.cureone.pharmacyandinventory.service.CategoryService;
import com.cureone.pharmacyandinventory.service.MedicineService;
import com.cureone.pharmacyandinventory.service.InventoryServices;
import com.cureone.pharmacyandinventory.service.BillingService;

/**
 * GUIContext
 * -----------
 * Central shared context for GUI layer.
 * Holds service instances and currently logged-in user.
 *
 * DO NOT put business logic here.
 */
public class GUIContext {

    // ---------- AUTH ----------
    public static AuthService authService;
    public static User loggedInUser;

    // ---------- SERVICES ----------
    public static AppointmentService appointmentService;
    public static DoctorService doctorService;
    public static PatientService patientService;
    public static MedicineService medicineService;
    public static InventoryServices inventoryService;
    public static BillingService billingService;
    public static CategoryService categoryService;
    // ---------- HELPERS ----------
    public static boolean isLoggedIn() {
        return loggedInUser != null;
    }

    public static void logout() {
        loggedInUser = null;
    }
}
