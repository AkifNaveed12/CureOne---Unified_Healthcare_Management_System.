package com.cureone.gui.panels;

import com.cureone.pharmacyandinventory.model.Category;
import com.cureone.pharmacyandinventory.service.CategoryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class CategoryManagementPanel extends JPanel {

    private final CategoryService service;
    private JTable table;
    private DefaultTableModel model;
    private JTextField search;

    public CategoryManagementPanel(CategoryService service) {
        this.service = service;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        add(buildTop(), BorderLayout.NORTH);
        add(buildTable(), BorderLayout.CENTER);

        loadAll();
    }

    // ================= TOP BAR =================
    private JPanel buildTop() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));

        search = new JTextField(15);

        JButton searchBtn = new JButton("Search");
        JButton addBtn = new JButton("Add");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Delete");
        JButton refreshBtn = new JButton("Refresh");

        p.add(new JLabel("Name:"));
        p.add(search);
        p.add(searchBtn);
        p.add(addBtn);
        p.add(updateBtn);
        p.add(deleteBtn);
        p.add(refreshBtn);

        searchBtn.addActionListener(e -> search());
        addBtn.addActionListener(e -> addCategory());
        updateBtn.addActionListener(e -> updateCategory());
        deleteBtn.addActionListener(e -> deleteCategory());
        refreshBtn.addActionListener(e -> loadAll());

        return p;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new String[]{"ID", "Name", "Description"}, 0
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
        List<Category> list = service.getAllCategories();

        for (Category c : list) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription()
            });
        }
    }

    // ================= SEARCH =================
    private void search() {
        model.setRowCount(0);
        List<Category> list = service.searchByName(search.getText());

        for (Category c : list) {
            model.addRow(new Object[]{
                    c.getId(),
                    c.getName(),
                    c.getDescription()
            });
        }
    }

    // ================= ADD =================
    private void addCategory() {
        Category c = showCategoryDialog(null);
        if (c != null) {
            service.addCategory(c);
            loadAll();
        }
    }

    // ================= UPDATE =================
    private void updateCategory() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Select a category first");
            return;
        }

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());
        String name = model.getValueAt(row, 1).toString();
        String desc = model.getValueAt(row, 2).toString();

        Category existing = new Category(name, desc, id);
        Category updated = showCategoryDialog(existing);

        if (updated != null) {
            service.deleteCategory(id);   // simple approach (safe)
            service.addCategory(updated);
            loadAll();
        }
    }

    // ================= DELETE =================
    private void deleteCategory() {
        int row = table.getSelectedRow();
        if (row == -1) return;

        int id = Integer.parseInt(model.getValueAt(row, 0).toString());

        int confirm = JOptionPane.showConfirmDialog(
                this, "Delete this category?", "Confirm",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteCategory(id);
            loadAll();
        }
    }

    // ================= DIALOG =================
    private Category showCategoryDialog(Category c) {
        JTextField name = new JTextField(c == null ? "" : c.getName());
        JTextArea desc = new JTextArea(c == null ? "" : c.getDescription(), 3, 20);

        JPanel p = new JPanel(new GridLayout(4, 1, 5, 5));
        p.add(new JLabel("Category Name:"));
        p.add(name);
        p.add(new JLabel("Description:"));
        p.add(new JScrollPane(desc));

        int res = JOptionPane.showConfirmDialog(
                this, p,
                c == null ? "Add Category" : "Update Category",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (res == JOptionPane.OK_OPTION) {
            return new Category(
                    name.getText(),
                    desc.getText(),
                    c == null ? 0 : c.getId()
            );
        }
        return null;
    }
}
