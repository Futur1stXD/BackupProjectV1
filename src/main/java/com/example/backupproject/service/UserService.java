package com.example.backupproject.service;

import com.example.backupproject.model.User;

import java.util.List;

public interface UserService {
    void register(User user);
    User findByUsername(String username);
    User findById(Long id);
}