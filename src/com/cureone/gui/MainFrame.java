//package com.cureone.gui;
//
//import com.cureone.gui.dashboards.AdminDashboard;
//import com.cureone.gui.dashboards.DoctorDashboard;
//import com.cureone.gui.dashboards.PatientDashboard;
//import com.cureone.gui.dashboards.PharmacistDashboard;
//import com.cureone.gui.dashboards.CustomerDashboard;
//
//
//import javax.swing.*;
//import java.awt.*;
//
///**
// * MainFrame
// * ----------
// * Single window for the entire CureOne application.
// * Uses CardLayout to switch between Login, Signup, and Dashboards.
// */
//public class MainFrame extends JFrame {
//
//    // Card names
//    public static final String LOGIN = "LOGIN";
//    public static final String SIGNUP = "SIGNUP";
//    public static final String ADMIN = "ADMIN";
//    public static final String DOCTOR = "DOCTOR";
//    public static final String PATIENT = "PATIENT";
//    public static final String PHARMACIST = "PHARMACIST";
//    public static final String CUSTOMER = "CUSTOMER";
//
//    private final CardLayout cardLayout;
//    private final JPanel container;
//
//    public MainFrame() {
//
//        setTitle("CureOne | Unified Healthcare Management");
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        setSize(1200, 700);
//        setLocationRelativeTo(null); // center screen
//        setLayout(new BorderLayout());
//
//        // Card container
//        cardLayout = new CardLayout();
//        container = new JPanel(cardLayout);
//
//        // Add base screens
//        container.add(new LoginPanel(this), LOGIN);
//        container.add(new SignupPanel(this), SIGNUP);
//
//
//
//        // ===================== START FIX AREA =====================
//        // Pre-register ALL dashboards ONCE
//        // This prevents CardLayout silent failures on Back button
//
//        container.add(new AdminDashboard(this), ADMIN);
//        container.add(new DoctorDashboard(this), DOCTOR);
//        container.add(new PatientDashboard(this), PATIENT);
//        container.add(new CustomerDashboard(this), CUSTOMER);
//        container.add(new PharmacistDashboard(this, 0), PHARMACIST);
//        // Pharmacist dashboard is role-specific (needs ID), so NOT preloaded
//
//        // ===================== END FIX AREA =====================
//
//
//
//        // Add container to frame
//        add(container, BorderLayout.CENTER);
//
//        // Show login by default
//        showScreen(LOGIN);
//    }
//
//    /* ===================== SCREEN NAVIGATION ===================== */
//
//    public void showScreen(String screen) {
//        cardLayout.show(container, screen);
//    }
//
//    /* ===================== DASHBOARD LOADERS ===================== */
//
//    public void loadAdminDashboard() {
////        container.add(new AdminDashboard(this), ADMIN);
////        showScreen(ADMIN);
//
//        showScreen(ADMIN);
//
//    }
//
//    public void loadDoctorDashboard() {
////        container.add(new DoctorDashboard(this), DOCTOR);
////        showScreen(DOCTOR);
//
//        showScreen(DOCTOR);
//    }
//    public void loadPatientDashboard() {
////        container.add(new PatientDashboard(this), PATIENT);
////        showScreen(PATIENT);
//
//        showScreen(PATIENT);
//
//    }
//
//    public void loadPharmacistDashboard(int pharmacistId) {
////        container.add(new PharmacistDashboard(this, pharmacistId), PHARMACIST);
////        showScreen(PHARMACIST);
//
//        showScreen(PHARMACIST);
//
//    }
//
//    public void loadCustomerDashboard() {
////        container.add(new CustomerDashboard(this), CUSTOMER);
////        showScreen(CUSTOMER);
//
//        showScreen(CUSTOMER);
//    }
//}

package com.cureone.gui;

import com.cureone.gui.dashboards.*;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public static final String LOGIN = "LOGIN";
    public static final String SIGNUP = "SIGNUP";
    public static final String ADMIN = "ADMIN";
    public static final String DOCTOR = "DOCTOR";
    public static final String PATIENT = "PATIENT";
    public static final String PHARMACIST = "PHARMACIST";
    public static final String CUSTOMER = "CUSTOMER";

    private final CardLayout cardLayout;
    private final JPanel container;

    public MainFrame() {
        setTitle("CureOne | Unified Healthcare Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        container.add(new LoginPanel(this), LOGIN);
        container.add(new SignupPanel(this), SIGNUP);

        add(container, BorderLayout.CENTER);
        showScreen(LOGIN);
    }

    public void showScreen(String screen) {
        cardLayout.show(container, screen);
    }

    // ================= FIX START =================
    private void resetAndShow(String key, JPanel panel) {
        for (Component c : container.getComponents()) {
            if (key.equals(container.getLayout().toString())) {
                container.remove(c);
                break;
            }
        }
        container.add(panel, key);
        cardLayout.show(container, key);
        container.revalidate();
        container.repaint();
    }
    // ================= FIX END =================

    public void loadAdminDashboard() {
        resetAndShow(ADMIN, new AdminDashboard(this));
    }

    public void loadDoctorDashboard() {
        resetAndShow(DOCTOR, new DoctorDashboard(this));
    }

    public void loadPatientDashboard() {
        resetAndShow(PATIENT, new PatientDashboard(this));
    }

    public void loadPharmacistDashboard(int pharmacistId) {
        resetAndShow(PHARMACIST, new PharmacistDashboard(this, pharmacistId));
    }

    public void loadCustomerDashboard() {
        resetAndShow(CUSTOMER, new CustomerDashboard(this));
    }
}


