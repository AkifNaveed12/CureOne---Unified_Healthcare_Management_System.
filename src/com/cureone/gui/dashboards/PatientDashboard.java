package com.cureone.gui.dashboards;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.common.Result;
import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.AppointmentPanel;
import com.cureone.patientsandrecords.model.Patient;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class PatientDashboard extends JPanel {

    private final MainFrame frame;
    private final JPanel contentPanel;

    public PatientDashboard(MainFrame frame) {
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        JPanel menu = new JPanel(new GridLayout(9, 1, 10, 10));
        menu.setPreferredSize(new Dimension(230, 0));
        menu.setBackground(new Color(30, 70, 100));

        JLabel title = new JLabel("PATIENT DASHBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton bookBtn = createBtn("Book Appointment");
        JButton viewBtn = createBtn("My Appointments");
        JButton historyBtn = createBtn("Medical Records");
        JButton profileBtn = createBtn("Update Profile");
        JButton logoutBtn = createBtn("Logout");

        menu.add(title);
        menu.add(bookBtn);
        menu.add(viewBtn);
        menu.add(historyBtn);
        menu.add(profileBtn);
        menu.add(new JLabel());
        menu.add(logoutBtn);

        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.add(
                new JLabel("Welcome Patient – Choose an option", SwingConstants.CENTER),
                BorderLayout.CENTER
        );

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        bookBtn.addActionListener(e -> bookAppointment());
        viewBtn.addActionListener(e -> showAppointments());
        historyBtn.addActionListener(e -> showMedicalRecords());
        profileBtn.addActionListener(e -> updateProfile());
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
    private Patient getCurrentPatient() {
        if (GUIContext.loggedInUser == null) return null;
        if (!"PATIENT".equalsIgnoreCase(GUIContext.loggedInUser.getRole())) return null;

        int patientId = GUIContext.loggedInUser.getLinkedId();
        if (patientId <= 0) return null;

        return GUIContext.patientService.getPatientById(patientId);
    }

    private void bookAppointment() {
        Patient p = getCurrentPatient();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextField doctorId = new JTextField();
        JTextField date = new JTextField("YYYY-MM-DD");
        JTextField time = new JTextField("HH:mm");
        JTextField duration = new JTextField();
        JTextField reason = new JTextField();

        panel.add(new JLabel("Doctor ID:"));
        panel.add(doctorId);
        panel.add(new JLabel("Date:"));
        panel.add(date);
        panel.add(new JLabel("Time:"));
        panel.add(time);
        panel.add(new JLabel("Duration (min):"));
        panel.add(duration);
        panel.add(new JLabel("Reason:"));
        panel.add(reason);

        if (JOptionPane.showConfirmDialog(this, panel, "Book Appointment",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            Result<Appointment> r =
                    GUIContext.appointmentService.bookAppointment(
                            p.getId(),
                            Integer.parseInt(doctorId.getText()),
                            LocalDate.parse(date.getText()),
                            time.getText(),
                            Integer.parseInt(duration.getText()),
                            reason.getText()
                    );

            JOptionPane.showMessageDialog(this, r.getMessage());
        }
    }

    private void showAppointments() {
        Patient p = getCurrentPatient();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found");
            return;
        }

        switchPanel(new AppointmentPanel(
                GUIContext.appointmentService,
                p.getId(),
                "PATIENT"
        ));
    }

    private void showMedicalRecords() {
        Patient p = getCurrentPatient();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found");
            return;
        }

        List<Appointment> list =
                GUIContext.appointmentService.getAppointmentsForPatient(p.getId());

        JTextArea area = new JTextArea();
        area.setEditable(false);

        if (list.isEmpty()) {
            area.setText("No medical history found.");
        } else {
            for (Appointment a : list) {
                area.append(
                        a.getAppointmentDate()
                                + " | Doctor ID: " + a.getDoctorId()
                                + " | Reason: " + a.getReason()
                                + "\n"
                );
            }
        }

        switchPanel(new JScrollPane(area));
    }

    private void updateProfile() {
        Patient p = getCurrentPatient();
        if (p == null) {
            JOptionPane.showMessageDialog(this, "Patient not found");
            return;
        }

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JTextField age = new JTextField(String.valueOf(p.getAge()));
        JTextField country = new JTextField(p.getCountry());
        JTextField contact = new JTextField(p.getContact());

        panel.add(new JLabel("Name (Locked):"));
        panel.add(new JLabel(p.getName()));
        panel.add(new JLabel("Gender (Locked):"));
        panel.add(new JLabel(p.getGender()));
        panel.add(new JLabel("Age:"));
        panel.add(age);
        panel.add(new JLabel("Country:"));
        panel.add(country);
        panel.add(new JLabel("Contact:"));
        panel.add(contact);

        if (JOptionPane.showConfirmDialog(this, panel, "Update Profile",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            p.setAge(Integer.parseInt(age.getText()));
            p.setCountry(country.getText());
            p.setContact(contact.getText());

            GUIContext.patientService.updatePatient(p);
            JOptionPane.showMessageDialog(this, "Profile updated");
        }
    }
}
