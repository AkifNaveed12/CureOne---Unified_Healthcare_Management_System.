package com.cureone.gui.panels;

import com.cureone.patientsandrecords.model.Patient;
import com.cureone.patientsandrecords.service.PatientService;
import com.cureone.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PatientManagementPanel extends JPanel {

    private final PatientService service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public PatientManagementPanel(PatientService service, MainFrame frame) {
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
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("Patient Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        searchField = new JTextField(12);
        JButton searchBtn = new JButton("Search");
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        addBtn.addActionListener(e -> addPatient());
        editBtn.addActionListener(e -> editPatient());
        deleteBtn.addActionListener(e -> deletePatient());
        refreshBtn.addActionListener(e -> loadAll());
        backBtn.addActionListener(e -> frame.showScreen(MainFrame.ADMIN));

        actions.add(new JLabel("Patient ID:"));
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
                new String[]{"ID","Name","Age","Gender","Country","Contact","Disease"}, 0
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
        List<Patient> list = service.getAllPatients();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-","No patients found","-","-","-","-","-"});
            return;
        }

        for (Patient p : list) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getName(),
                    p.getAge(),
                    p.getGender(),
                    p.getCountry(),
                    p.getContact(),
                    p.getDiseaseDescription()
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);
        try {
            int id = Integer.parseInt(searchField.getText().trim());
            Patient p = service.getPatientById(id);

            if (p == null) {
                JOptionPane.showMessageDialog(this, "Patient not found");
                return;
            }

            model.addRow(new Object[]{
                    p.getId(), p.getName(), p.getAge(),
                    p.getGender(), p.getCountry(),
                    p.getContact(), p.getDiseaseDescription()
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter valid Patient ID");
        }
    }

    // ================= ADD =================
    private void addPatient() {
        Patient p = showPatientDialog(null);
        if (p != null) {
            service.addPatient(p);
            loadAll();
        }
    }

    // ================= EDIT =================
    private void editPatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Patient p = service.getPatientById(id);

        Patient updated = showPatientDialog(p);
        if (updated != null) {
            service.updatePatient(updated);
            loadAll();
        }
    }

    // ================= DELETE =================
    private void deletePatient() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a patient first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        int c = JOptionPane.showConfirmDialog(this, "Delete patient?", "Confirm", JOptionPane.YES_NO_OPTION);

        if (c == JOptionPane.YES_OPTION) {
            service.deletePatient(id);
            loadAll();
        }
    }

    // ================= DIALOG =================
    private Patient showPatientDialog(Patient p) {
        JTextField name = new JTextField(p == null ? "" : p.getName());
        JTextField age = new JTextField(p == null ? "" : String.valueOf(p.getAge()));
        JTextField gender = new JTextField(p == null ? "" : p.getGender());
        JTextField country = new JTextField(p == null ? "" : p.getCountry());
        JTextField contact = new JTextField(p == null ? "" : p.getContact());
        JTextField disease = new JTextField(p == null ? "" : p.getDiseaseDescription());

        JPanel panel = new JPanel(new GridLayout(6,2,8,8));
        panel.add(new JLabel("Name")); panel.add(name);
        panel.add(new JLabel("Age")); panel.add(age);
        panel.add(new JLabel("Gender")); panel.add(gender);
        panel.add(new JLabel("Country")); panel.add(country);
        panel.add(new JLabel("Contact")); panel.add(contact);
        panel.add(new JLabel("Disease")); panel.add(disease);

        int res = JOptionPane.showConfirmDialog(
                this, panel,
                p == null ? "Add Patient" : "Edit Patient",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (res == JOptionPane.OK_OPTION) {
            Patient patient = p == null ? new Patient() : p;
            patient.setName(name.getText());
            patient.setAge(Integer.parseInt(age.getText()));
            patient.setGender(gender.getText());
            patient.setCountry(country.getText());
            patient.setContact(contact.getText());
            patient.setDiseaseDescription(disease.getText());
            return patient;
        }
        return null;
    }
}
