package com.cureone.gui.panels;

import com.cureone.appointmentsandscheduling.model.Appointment;
import com.cureone.appointmentsandscheduling.service.AppointmentService;
import com.cureone.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AppointmentManagementPanel extends JPanel {

    private final AppointmentService service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public AppointmentManagementPanel(AppointmentService service, MainFrame frame) {
        this.service = service;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }

    // ================= TOP =================
    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Appointment Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        searchField = new JTextField(10);
        JButton searchBtn = new JButton("Search");
        JButton cancelBtn = new JButton("Cancel");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        cancelBtn.addActionListener(e -> cancelAppointment());
        refreshBtn.addActionListener(e -> loadAll());
        backBtn.addActionListener(e -> frame.showScreen(MainFrame.ADMIN));

        actions.add(new JLabel("Doctor / Patient ID:"));
        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(cancelBtn);
        actions.add(refreshBtn);
        actions.add(backBtn);

        top.add(title, BorderLayout.WEST);
        top.add(actions, BorderLayout.EAST);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new String[]{
                        "ID", "Patient ID", "Doctor ID",
                        "Date", "Time", "Duration", "Reason", "Status"
                }, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        return new JScrollPane(table);
    }

    // ================= LOAD =================
    private void loadAll() {
        model.setRowCount(0);
        List<Appointment> list = service.getAllAppointments();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{
                    "-", "-", "-", "-", "-", "-", "No appointments", "-"
            });
            return;
        }

        for (Appointment a : list) {
            addRow(a);
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);

        try {
            int id = Integer.parseInt(searchField.getText().trim());
            List<Appointment> list = service.searchByDoctorOrPatient(id);

            if (list == null || list.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No appointments found");
                return;
            }

            for (Appointment a : list) {
                addRow(a);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid numeric ID");
        }
    }

    // ================= CANCEL =================
    private void cancelAppointment() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select an appointment first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Cancel this appointment?",
                "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteAppointment(id);
            loadAll();
        }
    }

    // ================= HELPER =================
    private void addRow(Appointment a) {
        model.addRow(new Object[]{
                a.getId(),
                a.getPatientId(),
                a.getDoctorId(),
                a.getAppointmentDate(),
                a.getAppointmentTime(),
                a.getDurationMinutes(),
                a.getReason(),
                a.getStatus()
        });
    }
}
