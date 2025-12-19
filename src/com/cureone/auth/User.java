package com.cureone.auth;

public class User {

    private int id;
    private String username;
    private String password;
    private String role;
    private int linkedId;   // already present, just unused

    public User(int id, String username, String password, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    // ===== EXISTING GETTERS =====
    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    // ===== REQUIRED FIX =====
    public int getLinkedId() {
        return linkedId;
    }

    public void setLinkedId(int linkedId) {
        this.linkedId = linkedId;
    }
}
