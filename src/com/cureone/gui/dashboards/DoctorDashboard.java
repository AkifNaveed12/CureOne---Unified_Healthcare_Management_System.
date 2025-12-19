package com.cureone.gui.dashboards;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.AppointmentPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DoctorDashboard extends JPanel {

    private final MainFrame frame;
    private final JPanel contentPanel;

    public DoctorDashboard(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== LEFT MENU =====
        JPanel menu = new JPanel(new GridLayout(8, 1, 10, 10));
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setBackground(new Color(30, 70, 100));

        JLabel title = new JLabel("DOCTOR DASHBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton apptBtn = createBtn("My Appointments");
        JButton recordsBtn = createBtn("Medical Records");
        JButton logoutBtn = createBtn("Logout");

        menu.add(title);
        menu.add(apptBtn);
        menu.add(recordsBtn);
        menu.add(new JLabel());
        menu.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(
                new JLabel("Welcome Doctor – Choose an option", SwingConstants.CENTER),
                BorderLayout.CENTER
        );

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        apptBtn.addActionListener(e -> showAppointments());
        recordsBtn.addActionListener(e -> showMedicalRecords());
        logoutBtn.addActionListener(e -> {
            GUIContext.logout();
            frame.showScreen(MainFrame.LOGIN);
        });
    }

    private JButton createBtn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(60, 120, 180));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void switchPanel(Component c) {
        contentPanel.removeAll();
        contentPanel.add(c, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    // ===== CORE FIX (USES linkedId ONLY) =====
    private Doctor getCurrentDoctor() {
        if (GUIContext.loggedInUser == null) return null;
        if (!"DOCTOR".equalsIgnoreCase(GUIContext.loggedInUser.getRole())) return null;

        int doctorId = GUIContext.loggedInUser.getLinkedId();
        if (doctorId <= 0) return null;

        return GUIContext.doctorService.getDoctorById(doctorId);
    }

    private void showAppointments() {
        Doctor d = getCurrentDoctor();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found");
            return;
        }

        switchPanel(new AppointmentPanel(
                GUIContext.appointmentService,
                d.getId(),
                "DOCTOR"
        ));
    }

    private void showMedicalRecords() {
        Doctor d = getCurrentDoctor();
        if (d == null) {
            JOptionPane.showMessageDialog(this, "Doctor not found");
            return;
        }

        List<Appointment> list =
                GUIContext.appointmentService.getAppointmentsForDoctor(d.getId());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        if (list.isEmpty()) {
            area.setText("No medical records found.");
        } else {
            for (Appointment a : list) {
                area.append(
                        a.getAppointmentDate()
                                + " | Patient ID: " + a.getPatientId()
                                + " | Reason: " + a.getReason()
                                + " | Status: " + a.getStatus()
                                + "\n"
                );
            }
        }

        switchPanel(new JScrollPane(area));
    }
}
