package com.cureone.gui.panels;

import com.cureone.common.Result;
import com.cureone.gui.GUIContext;
import com.cureone.pharmacyandinventory.model.Cart;
import com.cureone.pharmacyandinventory.model.CartItem;
import com.cureone.pharmacyandinventory.model.Invoice;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class CustomerCartPanel extends JPanel {

    private final Cart cart;
    private JTable table;
    private DefaultTableModel model;
    private JLabel totalLabel;

    public CustomerCartPanel(Cart cart) {
        this.cart = cart;

        setLayout(new BorderLayout());
        add(buildTable(), BorderLayout.CENTER);
        add(buildBottom(), BorderLayout.SOUTH);

        loadCart();
    }

    private JScrollPane buildTable() {
        model = new DefaultTableModel(
                new Object[]{"Medicine", "Qty", "Price", "Total"}, 0
        ) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        return new JScrollPane(table);
    }

    private JPanel buildBottom() {
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalLabel = new JLabel("Total: 0.0");

        JButton checkoutBtn = new JButton("Checkout");
        checkoutBtn.addActionListener(e -> checkout());

        bottom.add(totalLabel);
        bottom.add(checkoutBtn);

        return bottom;
    }

    private void loadCart() {
        model.setRowCount(0);

        if (cart.isEmpty()) return;

        for (CartItem item : cart.getItems()) {
            model.addRow(new Object[]{
                    item.getMedicineName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.lineTotal()
            });
        }
        totalLabel.setText("Total: " + cart.getTotal());
    }

    private void checkout() {
        if (cart.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart is empty");
            return;
        }

        Result<Invoice> result = GUIContext.billingService.checkout(
                cart,
                "Walk-in Customer",   // customer name
                0                     // pharmacistId (0 for customer)
        );

        if (!result.isSuccess()) {
            JOptionPane.showMessageDialog(this, result.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Invoice inv = result.getData();

        JOptionPane.showMessageDialog(
                this,
                "Checkout Successful\n\nInvoice No: " + inv.getInvoiceNumber() +
                        "\nTotal: " + inv.getTotalAmount(),
                "Invoice",
                JOptionPane.INFORMATION_MESSAGE
        );

        loadCart(); // cart already cleared by backend
    }
    public void refresh() {
        loadCart();
    }

}
