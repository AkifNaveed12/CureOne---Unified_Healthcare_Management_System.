package com.cureone.gui.panels;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.dashboards.AdminDashboard;
import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.model.Medicine;
import com.cureone.pharmacyandinventory.service.MedicineService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class MedicineManagementPanel extends JPanel {

    private final MedicineService service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    public MedicineManagementPanel(MedicineService service, MainFrame frame) {
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
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        top.setBackground(Color.WHITE);

        JLabel title = new JLabel("Medicine Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        searchField = new JTextField(14);

        JButton searchBtn = new JButton("Search");
        JButton expiredBtn = new JButton("Expired");
        JButton addBtn = new JButton("Add");
        JButton editBtn = new JButton("Edit");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        expiredBtn.addActionListener(e -> loadExpired());
        addBtn.addActionListener(e -> addMedicine());
        editBtn.addActionListener(e -> editMedicine());
        deleteBtn.addActionListener(e -> deleteMedicine());
        refreshBtn.addActionListener(e -> loadAll());
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


        actions.add(new JLabel("Name:"));
        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(expiredBtn);
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
                new Object[]{"ID", "Name", "Price", "Qty", "Expiry", "Category"}, 0
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

        List<Medicine> list = service.getAllMedicines();
        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-", "No medicines found", "-", "-", "-", "-"});
            return;
        }

        for (Medicine m : list) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getName(),
                    m.getPrice(),
                    m.getQuantity(),
                    m.getExpiryDate(),
                    m.getCategory() != null ? m.getCategory().getName() : "-"
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);
        String key = searchField.getText().trim();

        List<Medicine> list = service.searchByName(key);
        if (list.isEmpty()) {
            model.addRow(new Object[]{"-", "No results found", "-", "-", "-", "-"});
            return;
        }

        for (Medicine m : list) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getName(),
                    m.getPrice(),
                    m.getQuantity(),
                    m.getExpiryDate(),
                    m.getCategory() != null ? m.getCategory().getName() : "-"
            });
        }
    }

    // ================= EXPIRED =================
    private void loadExpired() {
        model.setRowCount(0);
        List<Medicine> list = service.getExpiredMedicines();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-", "No expired medicines", "-", "-", "-", "-"});
            return;
        }

        for (Medicine m : list) {
            model.addRow(new Object[]{
                    m.getId(),
                    m.getName(),
                    m.getPrice(),
                    m.getQuantity(),
                    m.getExpiryDate(),
                    m.getCategory() != null ? m.getCategory().getName() : "-"
            });
        }
    }

    // ================= ADD =================
    private void addMedicine() {
        Medicine m = showMedicineDialog(null);
        if (m != null) {
            service.addMedicine(m);
            loadAll();
        }
    }

    // ================= EDIT =================
    private void editMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a medicine first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        Medicine m = service.getMedicineById(id);

        Medicine updated = showMedicineDialog(m);
        if (updated != null) {
            service.updateMedicine(updated);
            loadAll();
        }
    }

    // ================= DELETE =================
    private void deleteMedicine() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        if (JOptionPane.showConfirmDialog(
                this, "Delete this medicine?", "Confirm",
                JOptionPane.YES_NO_OPTION
        ) == JOptionPane.YES_OPTION) {
            service.deleteMedicine(id);
            loadAll();
        }
    }

    // ================= DIALOG =================
    private Medicine showMedicineDialog(Medicine m) {

        JTextField name = new JTextField(m == null ? "" : m.getName());
        JTextField price = new JTextField(m == null ? "" : String.valueOf(m.getPrice()));
        JTextField qty = new JTextField(m == null ? "" : String.valueOf(m.getQuantity()));
        JTextField manu = new JTextField(m == null ? "" : m.getManufacturer());
        JTextField expiry = new JTextField(
                m == null || m.getExpiryDate() == null ? "" : m.getExpiryDate().toString()
        );

        JComboBox<Category> categoryBox = new JComboBox<>();
        for (Category c : GUIContext.categoryService.getAllCategories()) {
            categoryBox.addItem(c);
            if (m != null && m.getCategory() != null && c.getId() == m.getCategory().getId()) {
                categoryBox.setSelectedItem(c);
            }
        }

        JPanel p = new JPanel(new GridLayout(6, 2, 8, 8));
        p.add(new JLabel("Name")); p.add(name);
        p.add(new JLabel("Price")); p.add(price);
        p.add(new JLabel("Quantity")); p.add(qty);
        p.add(new JLabel("Manufacturer")); p.add(manu);
        p.add(new JLabel("Expiry Date (YYYY-MM-DD)")); p.add(expiry);
        p.add(new JLabel("Category")); p.add(categoryBox);

        if (JOptionPane.showConfirmDialog(this, p,
                m == null ? "Add Medicine" : "Edit Medicine",
                JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {

            Medicine med = (m == null ? new Medicine() : m);
            med.setName(name.getText());
            med.setPrice(Double.parseDouble(price.getText()));
            med.setQuantity(Integer.parseInt(qty.getText()));
            med.setManufacturer(manu.getText());

            if (!expiry.getText().isBlank())
                med.setExpiryDate(LocalDate.parse(expiry.getText()));

            med.setCategory((Category) categoryBox.getSelectedItem());
            return med;
        }
        return null;
    }
}
