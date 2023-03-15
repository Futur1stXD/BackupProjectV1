package com.example.backupproject.site.controller;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SiteController {
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new AuthenticationRequestDto());
        return "login";
    }
    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerRequest", new User());
        return "register";
    }
    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }
}
