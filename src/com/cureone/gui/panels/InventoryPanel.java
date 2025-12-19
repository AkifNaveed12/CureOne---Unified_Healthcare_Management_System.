package com.cureone.gui.panels;

import com.cureone.gui.MainFrame;
import com.cureone.gui.util.NavigationUtil;
import com.cureone.pharmacyandinventory.model.InventoryItem;
import com.cureone.pharmacyandinventory.service.InventoryServices;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class InventoryPanel extends JPanel {

    private final InventoryServices service;
    private final int userId;
    private final String role;
    private final MainFrame frame;

    private DefaultTableModel model;
    private JTextField searchField;

    public InventoryPanel(InventoryServices service, int userId, String role, MainFrame frame) {
        this.service = service;
        this.userId = userId;
        this.role = role;
        this.frame = frame;

        setLayout(new BorderLayout());
        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout());

        JLabel title = new JLabel("Inventory");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchField = new JTextField(12);

        JButton search = new JButton("Search");
        JButton refresh = new JButton("Refresh");
        JButton back = new JButton("Back");

        search.addActionListener(e -> search());
        refresh.addActionListener(e -> loadAll());
        back.addActionListener(e ->
                NavigationUtil.goBack(frame, role, userId)
        );

        right.add(new JLabel("Medicine:"));
        right.add(searchField);
        right.add(search);
        right.add(refresh);
        right.add(back);

        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);
        return top;
    }

    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Medicine", "Qty", "Min", "Price", "Location", "Expiry"}, 0
        );
        return new JScrollPane(new JTable(model));
    }

    private void loadAll() {
        model.setRowCount(0);
        List<InventoryItem> list = service.getAllItems();
        for (InventoryItem i : list) {
            model.addRow(new Object[]{
                    i.getItemId(),
                    i.getMedicine().getName(),
                    i.getQuantity(),
                    i.getMinimumStockLimit(),
                    i.getPrice(),
                    i.getLocation(),
                    i.getExpiryDate()
            });
        }
    }

    private void search() {
        String name = searchField.getText().trim();
        if (name.isEmpty()) {
            loadAll();
            return;
        }

        model.setRowCount(0);
        for (InventoryItem i : service.getAllItems()) {
            if (i.getMedicine().getName().toLowerCase().contains(name.toLowerCase())) {
                model.addRow(new Object[]{
                        i.getItemId(),
                        i.getMedicine().getName(),
                        i.getQuantity(),
                        i.getMinimumStockLimit(),
                        i.getPrice(),
                        i.getLocation(),
                        i.getExpiryDate()
                });
            }
        }
    }
}
