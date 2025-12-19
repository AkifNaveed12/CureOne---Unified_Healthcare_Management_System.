package com.cureone.gui.panels;

import com.cureone.gui.MainFrame;
import com.cureone.gui.util.NavigationUtil;
import com.cureone.pharmacyandinventory.model.Invoice;
import com.cureone.pharmacyandinventory.service.BillingService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class BillingPanel extends JPanel {

    private final BillingService service;
    private final int userId;
    private final String role;
    private final MainFrame frame;

    private DefaultTableModel model;
    private JTextField searchField;

    // ✅ Simple constructor (Customer / Admin read-only)
    public BillingPanel(BillingService service) {
        this(service, 0, "CUSTOMER", null);
    }


    public BillingPanel(BillingService service, int userId, String role, MainFrame frame) {
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

        JLabel title = new JLabel("Billing / Invoices");
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

        right.add(new JLabel("Invoice:"));
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
                new Object[]{"ID", "Invoice No", "Customer", "Date", "Total"}, 0
        );
        return new JScrollPane(new JTable(model));
    }

    private void loadAll() {
        model.setRowCount(0);
        List<Invoice> list = service.getAllInvoices();
        for (Invoice i : list) {
            model.addRow(new Object[]{
                    i.getId(),
                    i.getInvoiceNumber(),
                    i.getCustomerName(),
                    i.getCreatedAt(),
                    i.getTotalAmount()
            });
        }
    }

    private void search() {
        String no = searchField.getText().trim();
        if (no.isEmpty()) return;

        model.setRowCount(0);
        Invoice i = service.getInvoiceByNumber(no);
        if (i != null) {
            model.addRow(new Object[]{
                    i.getId(),
                    i.getInvoiceNumber(),
                    i.getCustomerName(),
                    i.getCreatedAt(),
                    i.getTotalAmount()
            });
        }
    }
}
