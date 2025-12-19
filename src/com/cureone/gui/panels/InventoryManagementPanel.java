package com.cureone.gui.panels;

import com.cureone.gui.MainFrame;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.service.InventoryServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryManagementPanel extends JPanel {

    private final InventoryServices service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;

    private JTextField searchField;
    private JTextField thresholdField;

    public InventoryManagementPanel(InventoryServices service, MainFrame frame) {
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
        top.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("Inventory Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        searchField = new JTextField(12);
        thresholdField = new JTextField(6);

        JButton searchBtn = new JButton("Search");
        JButton lowStockBtn = new JButton("Low Stock");
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        lowStockBtn.addActionListener(e -> loadLowStock());
        refreshBtn.addActionListener(e -> loadAll());
        backBtn.addActionListener(e -> frame.showScreen(MainFrame.ADMIN));

        actions.add(new JLabel("Search:"));
        actions.add(searchField);
        actions.add(searchBtn);

        actions.add(new JLabel("Threshold:"));
        actions.add(thresholdField);
        actions.add(lowStockBtn);

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
                new Object[]{"Item ID", "Medicine", "Quantity", "Price", "Location", "Expiry"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(table);
    }

    // ================= LOAD ALL =================
    private void loadAll() {
        model.setRowCount(0);
        List<InventoryItem> list = service.getAllItems();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-", "No inventory items", "-", "-", "-", "-"});
            return;
        }

        for (InventoryItem i : list) {
            model.addRow(new Object[]{
                    i.getItemId(),
                    i.getMedicine().getName(),
                    i.getQuantity(),
                    i.getPrice(),
                    i.getLocation(),
                    i.getExpiryDate()
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);
        String key = searchField.getText().trim().toLowerCase();

        List<InventoryItem> list = service.getAllItems();
        boolean found = false;

        for (InventoryItem i : list) {
            if (i.getMedicine().getName().toLowerCase().contains(key)) {
                model.addRow(new Object[]{
                        i.getMedicine().getId(),
                        i.getMedicine().getName(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getLocation(),
                        i.getExpiryDate()
                });
                found = true;
            }
        }

        if (!found) {
            model.addRow(new Object[]{"-", "No results found", "-", "-", "-", "-"});
        }
    }

    // ================= LOW STOCK =================
    private void loadLowStock() {
        model.setRowCount(0);

        int threshold;
        try {
            threshold = Integer.parseInt(thresholdField.getText().trim());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Enter a valid threshold number");
            return;
        }

        List<InventoryItem> list = service.getAllItems();
        boolean found = false;

        for (InventoryItem i : list) {
            if (i.getQuantity() <= threshold) {
                model.addRow(new Object[]{
                        i.getItemId(),
                        i.getMedicine().getName(),
                        i.getQuantity(),
                        i.getPrice(),
                        i.getLocation(),
                        i.getExpiryDate()
                });
                found = true;
            }
        }

        if (!found) {
            model.addRow(new Object[]{"-", "No low stock items", "-", "-", "-", "-"});
        }
    }
}
