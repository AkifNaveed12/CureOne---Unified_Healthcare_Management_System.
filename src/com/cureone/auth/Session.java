package com.cureone.auth;

/**
 * Tiny session/auth stub used only for the CLI.
 * Replace with real AuthService later (Hamad).
 */
public class Session {
    public enum Role { CUSTOMER, PHARMACIST }

    private final Role role;
    private final int userId;
    private final String username;

    public Session(Role role, int userId, String username) {
        this.role = role;
        this.userId = userId;
        this.username = username;
    }

    public Role getRole() { return role; }
    public int getUserId() { return userId; }
    public String getUsername() { return username; }
}
