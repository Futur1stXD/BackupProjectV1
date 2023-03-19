package com.example.backupproject.site.controller;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.model.User;
import com.example.backupproject.site.rest.SiteRestController;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.RedirectView;


import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Controller
public class SiteController {
    private SiteRestController site;

    public SiteController(SiteRestController site) {
        this.site = site;
    }

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
    @GetMapping("/user/files")
    public String getUserFiles(Model model, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("jwt_token"));
        List<ResponseFile> files = site.getAllFiles(token);
        try {
            System.out.println(files.get(0).getFileName());
            for (int i = 0; i < files.size(); i++) {
                String fileName = files.get(i).getFileName();
                String fileType = files.get(i).getFileType();
                byte[] content = Files.readAllBytes(Path.of("/Users/futur1st/Downloads/" + fileName));
                MultipartFile multipartFile = new MockMultipartFile(files.get(i).getFileName(),
                        files.get(i).getFileName(), files.get(i).getFileType(), content);
                site.uploadFile(multipartFile, request);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        model.addAttribute("logined", token);
        model.addAttribute("authorized", token);
        model.addAttribute("files", files);
        return "download";
    }
}
