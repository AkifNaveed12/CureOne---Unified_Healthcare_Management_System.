package com.cureone.gui.panels;

import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.appointmentsandscheduling.service.DoctorService;
import com.cureone.gui.MainFrame;
import com.cureone.gui.dashboards.AdminDashboard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorManagementPanel extends JPanel {

    private final DoctorService service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public DoctorManagementPanel(DoctorService service, MainFrame frame) {
        this.service = service;
        this.frame = frame;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }

    // ================= TOP BAR =================
    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(Color.WHITE);
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        JLabel title = new JLabel("Doctor Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchField = new JTextField(12);
        JButton searchBtn = new JButton("Search");

        addBtn.addActionListener(e -> addDoctor());
        editBtn.addActionListener(e -> editDoctor());
        deleteBtn.addActionListener(e -> deleteDoctor());
        refreshBtn.addActionListener(e -> loadAll());
        searchBtn.addActionListener(e -> search());
        //backBtn.addActionListener(e -> frame.showScreen(MainFrame.ADMIN));
        backBtn.addActionListener(e -> {
            Container c = getParent();
            while (c != null && !(c instanceof AdminDashboard)) {
                c = c.getParent();
            }
            if (c instanceof AdminDashboard ad) {
                ad.showHome();
            }
        });


        actions.add(new JLabel("Doctor ID:"));
        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(addBtn);
        actions.add(editBtn);
        actions.add(deleteBtn);
        actions.add(refreshBtn);
        actions.add(backBtn);

        top.add(title, BorderLayout.WEST);
        top.add(actions, BorderLayout.EAST);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Specialization", "Phone", "Email"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(table);
    }

    // ================= LOAD =================
    private void loadAll() {
        model.setRowCount(0);
        List<Doctor> list = service.getAllDoctors();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-", "No doctors found", "-", "-", "-"});
            return;
        }

        for (Doctor d : list) {
            model.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    d.getSpecialization(),
                    d.getPhone(),
                    d.getEmail()
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        String text = searchField.getText().trim();
        model.setRowCount(0);

        if (text.isEmpty()) {
            loadAll();
            return;
        }

        try {
            int id = Integer.parseInt(text);
            Doctor d = service.getDoctorById(id);

            if (d == null) {
                JOptionPane.showMessageDialog(this, "Doctor not found");
                return;
            }

            model.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    d.getSpecialization(),
                    d.getPhone(),
                    d.getEmail()
            });

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid Doctor ID",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }


    // ================= ADD =================
    private void addDoctor() {
        Doctor d = showDoctorDialog(null);
        if (d != null) {
            service.addDoctor(d);
            loadAll();
        }
    }

    // ================= EDIT =================
    // ================= EDIT =================
    private void editDoctor() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Doctor d = service.getDoctorById(id);

        Doctor updated = showDoctorDialog(d);
        if (updated != null) {
            service.updateDoctorProfile(
                    updated.getId(),
                    updated.getSpecialization(),
                    updated.getPhone(),
                    updated.getEmail()
            );
            loadAll();
        }

    }


    // ================= DELETE =================
    private void deleteDoctor() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a doctor first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this, "Delete this doctor?", "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteDoctor(id);
            loadAll();
        }
    }

    // ================= DIALOG =================
    private Doctor showDoctorDialog(Doctor d) {
        JTextField name = new JTextField(d == null ? "" : d.getName());
        JTextField spec = new JTextField(d == null ? "" : d.getSpecialization());
        JTextField phone = new JTextField(d == null ? "" : d.getPhone());
        JTextField email = new JTextField(d == null ? "" : d.getEmail());

        JPanel p = new JPanel(new GridLayout(4,2,8,8));
        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Specialization")); p.add(spec);
        p.add(new JLabel("Phone")); p.add(phone);
        p.add(new JLabel("Email")); p.add(email);

        int res = JOptionPane.showConfirmDialog(
                this, p, d == null ? "Add Doctor" : "Edit Doctor",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (res == JOptionPane.OK_OPTION) {
            Doctor doc = d == null ? new Doctor() : d;
            doc.setName(name.getText());
            doc.setSpecialization(spec.getText());
            doc.setPhone(phone.getText());
            doc.setEmail(email.getText());
            return doc;
        }
        return null;
    }
}
