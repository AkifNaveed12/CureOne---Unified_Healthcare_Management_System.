package com.cureone.gui.dashboards;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.*;

import javax.swing.*;
import java.awt.*;

public class AdminPharmacyDashboard extends JPanel {

    private final MainFrame frame;
    private final JPanel contentPanel;

    public AdminPharmacyDashboard(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ================= LEFT MENU =================
        JPanel menu = new JPanel(new GridLayout(14, 1, 10, 10));
        menu.setPreferredSize(new Dimension(240, 0));
        menu.setBackground(new Color(30, 45, 80));

        JLabel title = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));


        JLabel pharmacyLabel = new JLabel("PHARMACY", SwingConstants.CENTER);
        pharmacyLabel.setForeground(Color.LIGHT_GRAY);

        JButton categoryBtn = createBtn("Categories");
        JButton medicineBtn = createBtn("Medicines");
        JButton inventoryBtn = createBtn("Inventory");
        JButton billingBtn = createBtn("Billing");
        JButton backBtn = new JButton("Back");

        menu.add(title);
        menu.add(new JLabel()); // spacer
        menu.add(pharmacyLabel);
        menu.add(categoryBtn);
        menu.add(medicineBtn);
        menu.add(inventoryBtn);
        menu.add(billingBtn);
        menu.add(backBtn);
        menu.add(new JLabel());


        // ================= CONTENT =================
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(
                new JLabel("Pharmacy Management", SwingConstants.CENTER),
                BorderLayout.CENTER
        );
        contentPanel.setBackground(Color.WHITE);

        JLabel welcome = new JLabel(
                "Welcome Admin – Select a management option",
                SwingConstants.CENTER
        );
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        contentPanel.add(welcome, BorderLayout.CENTER);

        add(menu, BorderLayout.WEST);
        add(contentPanel, BorderLayout.CENTER);

        // ================= ACTIONS =================


        categoryBtn.addActionListener(e ->
                switchPanel(
                        new CategoryManagementPanel(GUIContext.categoryService)
                )
        );


        medicineBtn.addActionListener(e ->
                switchPanel(new MedicineManagementPanel(
                        GUIContext.medicineService,
                        frame
                ))
        );

        inventoryBtn.addActionListener(e ->
                switchPanel(new InventoryManagementPanel(
                        GUIContext.inventoryService,
                        frame
                ))
        );

        billingBtn.addActionListener(e ->
                switchPanel(new BillingManagementPanel(
                        GUIContext.billingService,
                        frame
                ))
        );
//        backBtn.addActionListener(e ->
//                frame.showScreen(MainFrame.ADMIN)
//        );

        backBtn.addActionListener(e -> {
            Container c = getParent();
            while (c != null && !(c instanceof AdminDashboard)) {
                c = c.getParent();
            }
            if (c instanceof AdminDashboard ad) {
                ad.showHome();
            }
        });


        //    temp change
        setFocusable(true);
        requestFocusInWindow();
    }

    // ================= HELPERS =================
    private JButton createBtn(String text) {
        JButton b = new JButton(text);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setBackground(new Color(45, 70, 140));
        b.setForeground(Color.WHITE);
        return b;
    }

    private void switchPanel(JPanel panel) {
        contentPanel.removeAll();
        contentPanel.add(panel, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();

        //    temp change
        panel.setFocusable(true);
        panel.requestFocusInWindow();
    }
}
