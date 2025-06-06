package com.example.user_service;


import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service

public class UserService {
    private final Map<Long, User> userMap = new HashMap<>();

    public UserService() {
        userMap.put(1L, new User(1L, "Ali", "ali@example.com,"));
        userMap.put(2L, new User(2L, "Ayşe", "ayse@example.com"));
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(userMap.values());
    }

    public User getUserById(Long id) {
        return userMap.get(id);
    }
}
