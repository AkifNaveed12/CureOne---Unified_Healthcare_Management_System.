package com.cureone.gui;

import com.cureone.auth.AuthService;

import javax.swing.*;
import java.awt.*;

/**
 * SignupPanel
 * -----------
 * Allows new users to create accounts.
 * Redirects back to LoginPanel after success.
 */
public class SignupPanel extends JPanel {

    private final MainFrame frame;

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleBox;
    private JLabel errorLabel;

    public SignupPanel(MainFrame frame) {
        this.frame = frame;
        initUI();
    }

    private void initUI() {

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Create Account", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(new Color(0, 102, 204));

        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);

        roleBox = new JComboBox<>(new String[]{
                "PATIENT",
                "DOCTOR",
                "PHARMACIST"
        });

        JButton signupBtn = new JButton("Sign Up");
        JButton backBtn = new JButton("Back to Login");

        signupBtn.setBackground(new Color(0, 153, 76));
        signupBtn.setForeground(Color.WHITE);

        backBtn.setBackground(Color.GRAY);
        backBtn.setForeground(Color.WHITE);

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
        add(new JLabel("Role:"), gbc);

        gbc.gridy++;
        add(roleBox, gbc);

        gbc.gridy++;
        add(errorLabel, gbc);

        gbc.gridy++; gbc.gridwidth = 1;
        add(signupBtn, gbc);

        gbc.gridx = 1;
        add(backBtn, gbc);

        // ===== Actions =====
        signupBtn.addActionListener(e -> signup());
        backBtn.addActionListener(e -> frame.showScreen(MainFrame.LOGIN));
    }

    private void signup() {

        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();
        String role = roleBox.getSelectedItem().toString();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required");
            return;
        }

        if (password.length() < 4) {
            errorLabel.setText("Password must be at least 4 characters");
            return;
        }

        try {
            GUIContext.authService.signup(username, password, role);

            JOptionPane.showMessageDialog(
                    this,
                    "Account created successfully!\nPlease login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            frame.showScreen(MainFrame.LOGIN);

        } catch (Exception ex) {
            errorLabel.setText("Username already exists");
        }
    }

}
