package com.cureone.gui.util;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;

public class NavigationUtil {

    public static void goBack(MainFrame frame, String role, int userId) {
//        switch (role.toUpperCase()) {
//            case "ADMIN" -> frame.loadAdminDashboard();
//            case "DOCTOR" -> frame.loadDoctorDashboard();
//            case "PATIENT" -> frame.loadPatientDashboard();
//            case "PHARMACIST" -> frame.loadPharmacistDashboard(GUIContext.loggedInUser.getId());
//            case "CUSTOMER" -> frame.loadCustomerDashboard();
//            default -> frame.showScreen(MainFrame.LOGIN);
//        }
        if (role == null) {
            System.err.println("Navigation failed: frame is NULL");
            return;
        }

        switch (role.toUpperCase()) {
            case "ADMIN" -> frame.showScreen(MainFrame.ADMIN);
            case "DOCTOR" -> frame.showScreen(MainFrame.DOCTOR);
            case "PATIENT" -> frame.showScreen(MainFrame.PATIENT);
            case "PHARMACIST" -> frame.showScreen(MainFrame.PHARMACIST);
            case "CUSTOMER" -> frame.showScreen(MainFrame.CUSTOMER);
            default -> frame.showScreen(MainFrame.LOGIN);
        }
    }
}
