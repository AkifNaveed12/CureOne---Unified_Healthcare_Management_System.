package com.cureone.gui.panels;

import com.cureone.patientsandrecords.model.Patient;
import com.cureone.patientsandrecords.service.PatientService;
import com.cureone.gui.MainFrame;
import com.cureone.gui.util.NavigationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * PatientPanel
 * ------------
 * Used by:
 * - AdminDashboard   → manage patients
 * - PatientDashboard → view own profile
 */
public class PatientPanel extends JPanel {

    private final PatientService service;
    private final MainFrame frame;
    private final int userId;
    private final String role;

    private JTable table;
    private DefaultTableModel model;


    public PatientPanel(
            PatientService service,
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

        JLabel title = new JLabel("Patients");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JButton refresh = new JButton("Refresh");
        refresh.addActionListener(e -> loadData());

        JButton back = new JButton("Back");
        back.addActionListener(e ->
                NavigationUtil.goBack(frame, role, userId)
        );

        top.add(title);
        top.add(refresh);
        top.add(back);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{
                        "ID", "Name", "Age",
                        "Gender", "Country", "Contact", "Disease"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
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
            if ("PATIENT".equalsIgnoreCase(role)) {
                Patient p = service.getPatientById(userId);
                if (p != null) {
                    addRow(p);
                } else {
                    showEmpty("Patient profile not found");
                }
                return;
            }

            List<Patient> list = service.getAllPatients();

            if (list == null || list.isEmpty()) {
                showEmpty("No patients found");
                return;
            }

            for (Patient p : list) {
                addRow(p);
            }

        } catch (Exception ex) {
            showError();
        }
    }

    private void addRow(Patient p) {
        model.addRow(new Object[]{
                p.getId(),
                safe(p.getName()),
                p.getAge(),
                safe(p.getGender()),
                safe(p.getCountry()),
                safe(p.getContact()),
                safe(p.getDiseaseDescription())
        });
    }

    // ================= HELPERS =================
    private String safe(String v) {
        return (v == null || v.isBlank()) ? "-" : v;
    }

    private void showEmpty(String msg) {
        model.addRow(new Object[]{
                "-", msg, "-", "-", "-", "-", "-"
        });
    }

    private void showError() {
        JOptionPane.showMessageDialog(
                this,
                "Patient data could not be loaded.\nPlease check database.",
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }
}
