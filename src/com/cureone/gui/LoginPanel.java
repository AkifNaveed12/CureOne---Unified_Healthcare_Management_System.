package com.cureone.gui;

import com.cureone.auth.AuthService;
import com.cureone.auth.User;

import javax.swing.*;
import java.awt.*;

/**
 * LoginPanel
 * ----------
 * FIXED VERSION
 * - Sets GUIContext.loggedInUser
 * - Uses role-based navigation
 * - Compatible with FIX-B3 dashboards
 */
public class LoginPanel extends JPanel {

    private final MainFrame frame;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JLabel errorLabel;

    public LoginPanel(MainFrame frame) {
        this.frame = frame;
        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("CureOne Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 28));
        title.setForeground(new Color(0, 102, 204));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        JButton loginBtn = new JButton("Login");
        JButton signupBtn = new JButton("Sign Up");
        JButton customerBtn = new JButton("Enter as Pharmacy Customer");

        loginBtn.setBackground(new Color(0, 102, 204));
        loginBtn.setForeground(Color.WHITE);

        signupBtn.setBackground(Color.GRAY);
        signupBtn.setForeground(Color.WHITE);

        customerBtn.setBackground(new Color(70, 130, 180));
        customerBtn.setForeground(Color.WHITE);

        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setForeground(Color.RED);

        // ===== Layout =====
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridy++;
        add(new JLabel("Username:"), gbc);

        gbc.gridy++;
        add(usernameField, gbc);

        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridy++;
        add(passwordField, gbc);

        gbc.gridy++;
        add(errorLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(loginBtn, gbc);

        gbc.gridx = 1;
        add(signupBtn, gbc);

        gbc.gridy++;
        add(customerBtn, gbc);

        // ===== Actions =====
        loginBtn.addActionListener(e -> login());
        signupBtn.addActionListener(e -> frame.showScreen(MainFrame.SIGNUP));
        customerBtn.addActionListener(e -> frame.loadCustomerDashboard());
    }

    private void login() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username and password are required");
            return;
        }

        AuthService auth = GUIContext.authService;
        User user = auth.login(username, password);

        if (user == null) {
            errorLabel.setText("Invalid credentials");
            return;
        }

        // 🔥 CORE FIX: STORE LOGGED-IN USER
        GUIContext.loggedInUser = user;

        String role = user.getRole().toUpperCase();

        switch (role) {
            case "ADMIN" -> frame.loadAdminDashboard();
            case "DOCTOR" -> frame.loadDoctorDashboard();
            case "PATIENT" -> frame.loadPatientDashboard();
            case "PHARMACIST" -> frame.loadPharmacistDashboard(user.getId());
            default -> frame.loadCustomerDashboard();
        }
    }
}
