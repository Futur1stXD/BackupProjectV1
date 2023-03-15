package com.example.backupproject.site.rest;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.User;
import com.example.backupproject.rest.AuthenticationRestController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class SiteRestController {
    private AuthenticationRestController authenticationRestController;

    public SiteRestController(AuthenticationRestController authenticationRestController) {
        this.authenticationRestController = authenticationRestController;
    }

    @PostMapping("/login")
    public void login(@ModelAttribute AuthenticationRequestDto requestDto) {
        authenticationRestController.login(requestDto);
    }
    @PostMapping("/register")
    public RedirectView register(@ModelAttribute User user) {
        if (authenticationRestController.register(user).getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new RedirectView("/register");
        }
        return new RedirectView("/login");
    }
}
