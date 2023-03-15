package com.example.backupproject.service;

import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.model.User;

import java.util.List;

public interface AdminService {
    void registerAdmin(User user);
    User findByUsername(String username);
    List<User> getAll();
    void delete(Long id);
    List<ResponseFile> getAllFiles();
}