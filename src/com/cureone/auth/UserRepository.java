package com.cureone.auth;

public interface UserRepository {
    User findByUsername(String username);
    void save(User user);
    int findLinkedIdByUsername(String username);
}
