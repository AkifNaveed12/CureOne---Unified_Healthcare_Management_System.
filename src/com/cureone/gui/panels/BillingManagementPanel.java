package com.cureone.gui.panels;

import com.cureone.pharmacyandinventory.model.Invoice;
import com.cureone.pharmacyandinventory.service.BillingService;
import com.cureone.gui.MainFrame;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class BillingManagementPanel extends JPanel {

    private final BillingService service;
    private final MainFrame frame;

    private JTable table;
    private DefaultTableModel model;
    private JTextField searchField;

    private static final DateTimeFormatter FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public BillingManagementPanel(BillingService service, MainFrame frame) {
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

        JLabel title = new JLabel("Billing Management");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actions.setBackground(Color.WHITE);

        searchField = new JTextField(14);
        JButton searchBtn = new JButton("Search");
        JButton refreshBtn = new JButton("Refresh");
        JButton backBtn = new JButton("Back");

        searchBtn.addActionListener(e -> search());
        refreshBtn.addActionListener(e -> loadAll());
        //backBtn.addActionListener(e -> frame.showScreen(MainFrame.ADMIN));
        // ===== START FIX =====
        backBtn.addActionListener(e -> handleBack());
// ===== END FIX =====


        actions.add(new JLabel("Invoice No:"));
        actions.add(searchField);
        actions.add(searchBtn);
        actions.add(refreshBtn);
        actions.add(backBtn);

        top.add(title, BorderLayout.WEST);
        top.add(actions, BorderLayout.EAST);

        return top;
    }

    // ================= TABLE =================
    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{
                        "ID",
                        "Invoice Number",
                        "Customer",
                        "Date",
                        "Total Amount"
                }, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(24);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        return new JScrollPane(table);
    }

    // ================= LOAD ALL =================
    private void loadAll() {
        model.setRowCount(0);
        List<Invoice> list = service.getAllInvoices();

        if (list == null || list.isEmpty()) {
            model.addRow(new Object[]{"-", "-", "No invoices found", "-", "-"});
            return;
        }

        for (Invoice i : list) {
            addRow(i);
        }
    }

    // ================= SEARCH =================
    private void search() {
        String key = searchField.getText().trim();

        if (key.isEmpty()) {
            loadAll();
            return;
        }

        model.setRowCount(0);

        Invoice invoice = service.getInvoiceByNumber(key);

        if (invoice == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Invoice not found",
                    "Info",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        addRow(invoice);
    }

    // ================= ROW =================
    private void addRow(Invoice i) {
        model.addRow(new Object[]{
                i.getId(),
                i.getInvoiceNumber(),
                (i.getCustomerName() == null || i.getCustomerName().isBlank())
                        ? "Walk-in Customer"
                        : i.getCustomerName(),
                i.getCreatedAt() == null ? "-" : i.getCreatedAt().format(FMT),
                i.getTotalAmount()
        });
    }
    // ================= BACK HANDLER =================
    private void handleBack() {
        Container c = this.getParent();

        while (c != null) {
            if (c instanceof com.cureone.gui.dashboards.PharmacistDashboard p) {
                p.showHome();
                return;
            }
            if (c instanceof com.cureone.gui.dashboards.AdminDashboard a) {
                a.showHome();
                return;
            }
            c = c.getParent();
        }
    }

}
