package com.example.backupproject.service.impl;

import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.model.Role;
import com.example.backupproject.model.Status;
import com.example.backupproject.model.User;
import com.example.backupproject.repository.ResponseFileRepository;
import com.example.backupproject.repository.UserRepository;
import com.example.backupproject.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ResponseFileRepository responseFileRepository;
    public AdminServiceImpl(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, ResponseFileRepository responseFileRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.responseFileRepository = responseFileRepository;
    }
    @Override
    public void registerAdmin(User admin) {
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole(Role.ADMIN);
        admin.setStatus(Status.ACTIVE);
        User registeredUser = userRepository.save(admin);
        log.info("IN register - admin: {} successfully registered", registeredUser);
    }
    @Override
    public User findByUsername(String username) {
        User result = userRepository.findUserByEmail(username);
        log.info("IN findByUsername - user {} found by username: {}", result, username);
        return result;
    }

    @Override
    public List<User> getAll() {
        List<User> result = userRepository.findAll();
        log.info("IN getAll - {} users found", result.size());
        return result;
    }
    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
        log.info("IN delete - user with id: {} successfully deleted", id);
    }

    @Override
    public List<ResponseFile> getAllFiles() {
        return responseFileRepository.findAll();
    }
}