package com.cureone.gui.dashboards;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.*;

import javax.swing.*;
import java.awt.*;

/**
 * AdminDashboard
 * --------------
 * Full control panel for Admin
 */
public class AdminDashboard extends JPanel {

    private final MainFrame frame;
    private final JPanel contentPanel;

    public AdminDashboard(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================= LEFT MENU =================
        JPanel menu = new JPanel(new GridLayout(8, 1, 10, 10));
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setBackground(new Color(30, 45, 80));

        JLabel title = new JLabel("ADMIN PANEL", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton doctorBtn = createBtn("Doctors");
        JButton patientBtn = createBtn("Patients");
        JButton appointmentBtn = createBtn("Appointments");
        JButton pharmacyBtn = createBtn("Pharmacy");
        JButton logoutBtn = createBtn("Logout");

        menu.add(title);
        menu.add(doctorBtn);
        menu.add(patientBtn);
        menu.add(appointmentBtn);
        menu.add(pharmacyBtn);
        menu.add(new JLabel());
        menu.add(logoutBtn);

        // ================= CONTENT =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel welcome = new JLabel(
                "Welcome Admin – Select an option",
                SwingConstants.CENTER
        );
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        contentPanel.add(welcome, BorderLayout.CENTER);

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // ================= ACTIONS =================

        // Doctors (FULL CRUD)
        doctorBtn.addActionListener(e ->
                switchPanel(
                        new DoctorManagementPanel(
                                GUIContext.doctorService,
                                frame
                        )
                )
        );

        // Patients
        patientBtn.addActionListener(e ->
                switchPanel(
                        new PatientManagementPanel(
                                GUIContext.patientService,
                                frame
                        )
                )
        );

        // Appointments
        appointmentBtn.addActionListener(e ->
                switchPanel(
                        new AppointmentManagementPanel(
                                GUIContext.appointmentService,
                                frame
                        )
                )
        );
        pharmacyBtn.addActionListener(e ->
                switchPanel(new AdminPharmacyDashboard(frame))
        );


        logoutBtn.addActionListener(e -> frame.showScreen(MainFrame.LOGIN));
    }

    // ================= HELPERS =================
    private JButton createBtn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(45, 70, 140));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
