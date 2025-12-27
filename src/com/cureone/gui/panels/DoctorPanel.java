package com.cureone.gui.panels;

import com.cureone.appointmentsandscheduling.model.Doctor;
import com.cureone.appointmentsandscheduling.service.DoctorService;
import com.cureone.gui.MainFrame;
import com.cureone.gui.dashboards.AdminDashboard;
import com.cureone.gui.dashboards.DoctorDashboard;
import com.cureone.gui.util.NavigationUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class DoctorPanel extends JPanel {

    private final DoctorService service;
    private final int userId;
    private final String role;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;

    public DoctorPanel(DoctorService service, int userId, String role, MainFrame frame) {
        this.service = service;
        this.userId = userId;
        this.role = role;
        this.frame = frame;

        setLayout(new BorderLayout());
        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadData();
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JLabel title = new JLabel("Doctors");
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
        top.add(back);
        return top;
    }

    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Name", "Specialization", "Phone", "Email"}, 0
        );
        table = new JTable(model);
        return new JScrollPane(table);
    }

    private void loadData() {
        model.setRowCount(0);

        if ("DOCTOR".equalsIgnoreCase(role)) {
            Doctor d = service.getDoctorById(userId);
            if (d != null) addRow(d);
            return;
        }

        List<Doctor> list = service.getAllDoctors();
        for (Doctor d : list) addRow(d);
    }

    private void addRow(Doctor d) {
        model.addRow(new Object[]{
                d.getId(), d.getName(), d.getSpecialization(),
                d.getPhone(), d.getEmail()
        });
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
            c = c.getParent();
        }
    }

}
