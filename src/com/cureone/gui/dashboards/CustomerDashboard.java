package com.cureone.gui.dashboards;

import com.cureone.gui.GUIContext;
import com.cureone.gui.MainFrame;
import com.cureone.gui.panels.CustomerCartPanel;
import com.cureone.gui.panels.CustomerMedicinePanel;
import com.cureone.pharmacyandinventory.model.Cart;

import javax.swing.*;
import java.awt.*;

public class CustomerDashboard extends JPanel {

    private final MainFrame frame;
    private final CardLayout cardLayout = new CardLayout();
    private final JPanel contentPanel = new JPanel(cardLayout);
    private CustomerCartPanel cartPanel;

    // SINGLE cart for this customer session
    private final Cart cart = new Cart();

    // Screen keys
    private static final String MEDICINES = "MEDICINES";
    private static final String CART = "CART";

    public CustomerDashboard(MainFrame frame) {
        this.frame = frame;
        setLayout(new BorderLayout());

        add(buildSidebar(), BorderLayout.WEST);
        add(buildContent(), BorderLayout.CENTER);
    }

    // ================= SIDEBAR =================
    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setPreferredSize(new Dimension(240, 0));
        sidebar.setLayout(new GridLayout(6, 1, 0, 12));
        sidebar.setBackground(new Color(27, 44, 73)); // SAME AS ADMIN

        JButton medsBtn = createNavButton("View Medicines");
        JButton cartBtn = createNavButton("My Cart");
        JButton logoutBtn = createNavButton("Logout");

        medsBtn.addActionListener(e -> cardLayout.show(contentPanel, MEDICINES));
        cartBtn.addActionListener(e -> {cartPanel.refresh();
            cardLayout.show(contentPanel, CART);});
        logoutBtn.addActionListener(e -> frame.showScreen(MainFrame.LOGIN));

        sidebar.add(Box.createVerticalStrut(20));
        sidebar.add(medsBtn);
        sidebar.add(cartBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    // ================= CONTENT =================
    private JPanel buildContent() {

        contentPanel.add(
                new CustomerMedicinePanel(
                        GUIContext.medicineService,
                        cart
                ),
                MEDICINES
        );

        contentPanel.add(
                new CustomerCartPanel(cart),
                CART
        );

        cartPanel = new CustomerCartPanel(cart);
        contentPanel.add(cartPanel, CART);

        cardLayout.show(contentPanel, MEDICINES);
        return contentPanel;
    }

    // ================= BUTTON STYLE =================
    private JButton createNavButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(52, 86, 139));
        btn.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}
