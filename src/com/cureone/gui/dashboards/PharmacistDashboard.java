package com.cureone.gui.dashboards;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.*;

import javax.swing.*;
import java.awt.*;

public class PharmacistDashboard extends JPanel {

    private final MainFrame frame;
    private final int pharmacistId;
    private final JPanel contentPanel;

    public PharmacistDashboard(MainFrame frame, int pharmacistId) {
        this.frame = frame;
        this.pharmacistId = pharmacistId;

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== LEFT MENU =====
        JPanel menu = new JPanel(new GridLayout(8, 1, 10, 10));
        menu.setPreferredSize(new Dimension(240, 0));
        menu.setBackground(new Color(45, 80, 120));

        JLabel title = new JLabel("PHARMACIST PANEL", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JButton medicineBtn = createBtn("Medicines");
        JButton inventoryBtn = createBtn("Inventory");
        JButton billingBtn = createBtn("Billing / Invoices");
        JButton logoutBtn = createBtn("Logout");

        menu.add(title);
        menu.add(medicineBtn);
        menu.add(inventoryBtn);
        menu.add(billingBtn);
        menu.add(new JLabel());
        menu.add(logoutBtn);

        // ===== CONTENT =====
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);

        JLabel welcome = new JLabel(
                "Welcome Pharmacist – Choose an option",
                SwingConstants.CENTER
        );
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        contentPanel.add(welcome, BorderLayout.CENTER);

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // ===== ACTIONS =====
        medicineBtn.addActionListener(e ->
                switchPanel(
                        new MedicineManagementPanel(
                                GUIContext.medicineService,
                                frame
                        )
                )
        );

        inventoryBtn.addActionListener(e ->
                switchPanel(
                        new InventoryPanel(
                                GUIContext.inventoryService,
                                pharmacistId,
                                "PHARMACIST",
                                frame
                        )
                )
        );

        billingBtn.addActionListener(e ->
                switchPanel(
                        new BillingManagementPanel(
                                GUIContext.billingService,
                                frame
                        )
                )
        );

        logoutBtn.addActionListener(e -> frame.showScreen(MainFrame.LOGIN));
    }

    // ===== HELPERS =====
    private JButton createBtn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(70, 130, 190));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
