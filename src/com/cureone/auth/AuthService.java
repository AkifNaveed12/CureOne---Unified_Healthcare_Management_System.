package com.cureone.auth;

public class AuthService {

    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public User login(String username, String password) {
        User user = userRepo.findByUsername(username);

        if (user != null && user.getPassword().equals(password)) {

            // 🔥 CRITICAL FIX
            // userRepo MUST already load linked_id from DB into User
            // If not, this line ensures dashboards work
            user.setLinkedId(userRepo.findLinkedIdByUsername(username));

            return user;
        }
        return null;
    }

    public void signup(String username, String password, String role) {

        if (!isValidPassword(password)) {
            throw new IllegalArgumentException(
                    "Password must be 8+ chars, include upper, lower, digit & symbol"
            );
        }

        if (userRepo.findByUsername(username) != null) {
            throw new IllegalArgumentException("Username already exists.");
        }

        userRepo.save(new User(0, username, password, role));
    }

    public static boolean isValidPassword(String p) {
        return p.length() >= 8 &&
                p.matches(".*[A-Z].*") &&
                p.matches(".*[a-z].*") &&
                p.matches(".*\\d.*") &&
                p.matches(".*[@#$%!].*");
    }
}
