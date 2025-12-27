package com.cureone.gui.panels;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.gui.MainFrame;
import com.cureone.gui.dashboards.AdminDashboard;
import com.cureone.gui.dashboards.DoctorDashboard;
import com.cureone.gui.dashboards.PatientDashboard;
import com.cureone.gui.util.NavigationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

/**
 * AppointmentPanel
 * ----------------
 * Used by:
 * - AdminDashboard
 * - DoctorDashboard
 * - PatientDashboard
 */
public class AppointmentPanel extends JPanel {

    private final AppointmentService service;
    private final MainFrame frame;
    private final int userId;
    private final String role;

    private JTable table;
    private DefaultTableModel model;

    // ✅ Backward-compatible constructor (NO navigation)
    public AppointmentPanel(
            AppointmentService service,
            int userId,
            String role
    ) {
        this(service, userId, role, null);
    }


    public AppointmentPanel(
            AppointmentService service,
            int userId,
            String role,
            MainFrame frame
    ) {
        this.service = service;
        this.userId = userId;
        this.role = role;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadData();
    }


    // ================= TOP BAR =================
    private JPanel buildTop() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("Appointments");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());

        JButton back = new JButton("Back");
        back.addActionListener(e -> handleBack());
//        back.addActionListener(e ->
//                NavigationUtil.goBack(frame, role, userId)
//        );
        top.add(title);
        top.add(refresh);

        if ("PATIENT".equalsIgnoreCase(role)) {
            JButton bookBtn = new JButton("Book Appointment");
            bookBtn.addActionListener(e -> openBookingDialog());
            top.add(bookBtn);
        }

        top.add(back);
        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{
                        "ID", "Patient ID", "Doctor ID",
                        "Date", "Time", "Duration", "Reason", "Status"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    // ================= DATA =================
    private void loadData() {
        model.setRowCount(0);

        try {
            List<Appointment> list;

            if ("ADMIN".equalsIgnoreCase(role)) {
                list = service.getAllAppointments();
            }
            else if ("DOCTOR".equalsIgnoreCase(role)) {
                list = service.getAppointmentsForDoctor(userId);
            }
            else if ("PATIENT".equalsIgnoreCase(role)) {
                list = service.getAppointmentsForPatient(userId);
            }
            else {
                list = List.of();
            }

            if (list == null || list.isEmpty()) {
                showEmpty("No appointments found");
                return;
            }

            for (Appointment a : list) {
                model.addRow(new Object[]{
                        a.getId(),
                        a.getPatientId(),
                        a.getDoctorId(),
                        a.getAppointmentDate(),
                        a.getAppointmentTime(),
                        a.getDurationMinutes(),
                        safe(a.getReason()),
                        a.getStatus()
                });
            }

        } catch (Exception ex) {
            showError();
        }
    }

    // ================= BOOKING =================
    private void openBookingDialog() {
        JDialog dialog = new JDialog();
        dialog.setTitle("Book Appointment");
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(7, 2, 8, 8));

        JTextField doctorId = new JTextField();
        JTextField date = new JTextField("YYYY-MM-DD");
        JTextField time = new JTextField("HH:mm");
        JTextField duration = new JTextField("30");
        JTextField reason = new JTextField();

        dialog.add(new JLabel("Doctor ID"));
        dialog.add(doctorId);
        dialog.add(new JLabel("Date"));
        dialog.add(date);
        dialog.add(new JLabel("Time"));
        dialog.add(time);
        dialog.add(new JLabel("Duration (min)"));
        dialog.add(duration);
        dialog.add(new JLabel("Reason"));
        dialog.add(reason);

        JButton save = new JButton("Book");
        save.addActionListener(e -> {
            try {
                service.bookAppointment(
                        userId,
                        Integer.parseInt(doctorId.getText()),
                        LocalDate.parse(date.getText()),
                        time.getText(),
                        Integer.parseInt(duration.getText()),
                        reason.getText()
                );
                JOptionPane.showMessageDialog(dialog, "Appointment booked!");
                dialog.dispose();
                loadData();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                        dialog,
                        "Invalid input",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });

        dialog.add(new JLabel());
        dialog.add(save);
        dialog.setVisible(true);
    }

    // ================= HELPERS =================
    private String safe(String v) {
        return (v == null || v.isBlank()) ? "-" : v;
    }

    private void showEmpty(String msg) {
        model.addRow(new Object[]{
                "-", msg, "-", "-", "-", "-", "-", "-"
        });
    }

    private void showError() {
        JOptionPane.showMessageDialog(
                this,
                "Appointments could not be loaded.\nCheck database.",
                "Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // ================= BACK HANDLER =================
    private void handleBack() {
        Container c = this.getParent();

        while (c != null) {
            if (c instanceof AdminDashboard a) {
                a.showHome();
                return;
            }
            if (c instanceof DoctorDashboard d) {
                d.showHome();
                return;
            }
            if (c instanceof PatientDashboard p) {
                p.showHome();
                return;
            }
            c = c.getParent();
        }
    }

}
