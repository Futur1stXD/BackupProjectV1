package com.example.backupproject.site.controller;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class SiteController {
    @GetMapping("/login")
    public String getLoginPage(Model model) {
        model.addAttribute("loginRequest", new AuthenticationRequestDto());
        return "login.html";
    }

    @GetMapping("/register")
    public String getRegisterPage(Model model) {
        model.addAttribute("registerRequest", new User());
        return "register";
    }

    @GetMapping("/main")
    public String getMainPage(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("jwt_token"));
        model.addAttribute("logined", token);
        model.addAttribute("authorized", token);
        return "main";
    }
    @GetMapping("/logout")
    public RedirectView getLogout(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("jwt_token", null);
        return new RedirectView("/main");
    }
}
