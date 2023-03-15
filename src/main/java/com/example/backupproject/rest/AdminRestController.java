package com.example.backupproject.rest;

import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.model.User;
import com.example.backupproject.service.AdminService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/admin/")
public class AdminRestController {
    private AdminService adminService;
    public AdminRestController(AdminService adminService) {
        this.adminService = adminService;
    }
    @PostMapping("/register")
    public RedirectView register(@RequestBody User admin) {
        User userCheck = adminService.findByUsername(admin.getEmail());
        if (!(userCheck == null)) {
            throw new BadCredentialsException("This admin with this email: " + admin.getEmail() + " is already exist!");
        }
        adminService.registerAdmin(admin);
        return new RedirectView("/login");
    }
    @GetMapping("/users")
    public List<User> allUsers() {
        return adminService.getAll();
    }
    @GetMapping("/files")
    public List<ResponseFile> allFiles() {
        return adminService.getAllFiles();
    }
}