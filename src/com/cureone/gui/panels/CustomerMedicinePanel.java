package com.cureone.gui.panels;

import com.cureone.common.Result;
import com.cureone.gui.GUIContext;
import com.cureone.gui.dashboards.CustomerDashboard;
import com.cureone.pharmacyandinventory.model.Cart;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.MedicineService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CustomerMedicinePanel extends JPanel {

    private final MedicineService service;
    private final Cart cart;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public CustomerMedicinePanel(MedicineService service, Cart cart) {
        this.service = service;
        this.cart = cart;

        setLayout(new BorderLayout());
        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }

    // ================= TOP =================
    private JPanel buildTop() {
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));

        searchField = new JTextField(15);
        JButton searchBtn = new JButton("Search");
        JButton addBtn = new JButton("Add to Cart");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(ev -> search());
        addBtn.addActionListener(ev -> addToCart());
        backBtn.addActionListener(e -> handleBack());


        top.add(new JLabel("Medicine Name:"));
        top.add(searchField);
        top.add(searchBtn);
        top.add(addBtn);
        top.add(backBtn);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Price", "Available"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        return new JScrollPane(table);
    }

    // ================= LOAD =================
    private void loadAll() {
        model.setRowCount(0);
        List<Medicine> medicines = service.getAllMedicines();

        for (Medicine m : medicines) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getName(),
                    m.getPrice(),
                    m.getQuantity()
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);
        String key = searchField.getText().trim().toLowerCase();

        for (Medicine m : service.getAllMedicines()) {
            if (m.getName().toLowerCase().contains(key)) {
                model.addRow(new Object[]{
                        m.getId(),
                        m.getName(),
                        m.getPrice(),
                        m.getQuantity()
                });
            }
        }
    }

    // ================= ADD TO CART =================
    private void addToCart() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine first");
            return;
        }

        int medicineId = Integer.parseInt(model.getValueAt(row, 0).toString());

        String qtyStr = JOptionPane.showInputDialog(this, "Enter quantity:");
        if (qtyStr == null) return;

        int qty;
        try {
            qty = Integer.parseInt(qtyStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid quantity");
            return;
        }

        Result<String> result =
                GUIContext.billingService.addToCart(cart, medicineId, qty);

        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this, "Added to cart");
    }
    // ================= BACK HANDLER =================
    private void handleBack() {
        Container c = this.getParent();

        while (c != null) {
            if (c instanceof CustomerDashboard cd) {
                cd.showHome();
                return;
            }
            c = c.getParent();
        }
    }

}
