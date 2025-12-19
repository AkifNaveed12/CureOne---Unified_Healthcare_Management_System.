package com.cureone.gui;

import com.cureone.gui.dashboards.AdminDashboard;
import com.cureone.gui.dashboards.DoctorDashboard;
import com.cureone.gui.dashboards.PatientDashboard;
import com.cureone.gui.dashboards.PharmacistDashboard;
import com.cureone.gui.dashboards.CustomerDashboard;


import javax.swing.*;
import java.awt.*;

/**
 * MainFrame
 * ----------
 * Single window for the entire CureOne application.
 * Uses CardLayout to switch between Login, Signup, and Dashboards.
 */
public class MainFrame extends JFrame {

    // Card names
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
        setLocationRelativeTo(null); // center screen
        setLayout(new BorderLayout());

        // Card container
        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // Add base screens
        container.add(new LoginPanel(this), LOGIN);
        container.add(new SignupPanel(this), SIGNUP);

        // Add container to frame
        add(container, BorderLayout.CENTER);

        // Show login by default
        showScreen(LOGIN);
    }

    /* ===================== SCREEN NAVIGATION ===================== */

    public void showScreen(String screen) {
        cardLayout.show(container, screen);
    }

    /* ===================== DASHBOARD LOADERS ===================== */

    public void loadAdminDashboard() {
        container.add(new AdminDashboard(this), ADMIN);
        showScreen(ADMIN);
    }

    public void loadDoctorDashboard() {
        container.add(new DoctorDashboard(this), DOCTOR);
        showScreen(DOCTOR);
    }
    public void loadPatientDashboard() {
        container.add(new PatientDashboard(this), PATIENT);
        showScreen(PATIENT);
    }

    public void loadPharmacistDashboard(int pharmacistId) {
        container.add(new PharmacistDashboard(this, pharmacistId), PHARMACIST);
        showScreen(PHARMACIST);
    }

    public void loadCustomerDashboard() {
        container.add(new CustomerDashboard(this), CUSTOMER);
        showScreen(CUSTOMER);
    }
}
