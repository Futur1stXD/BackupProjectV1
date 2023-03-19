package com.example.backupproject.site.rest;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.FileBackup;
import com.example.backupproject.model.ResponseFile;
import com.example.backupproject.model.User;
import com.example.backupproject.rest.AuthenticationRestController;
import com.example.backupproject.rest.FileBackupController;
import com.example.backupproject.security.jwt.JwtTokenProvider;
import com.example.backupproject.service.FileService;
import com.example.backupproject.site.FileServiceSite.FileServiceSite;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.servlet.view.RedirectView;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Controller
public class SiteRestController {
    private AuthenticationRestController authenticationRestController;
    private FileServiceSite fileService;
    private JwtTokenProvider jwtTokenProvider;
    public SiteRestController(AuthenticationRestController authenticationRestController, FileServiceSite fileService, JwtTokenProvider jwtTokenProvider) {
        this.authenticationRestController = authenticationRestController;
        this.fileService = fileService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/login")
    public RedirectView login(@ModelAttribute AuthenticationRequestDto requestDto, HttpServletRequest request) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(authenticationRestController.login(requestDto).getBody());
        JsonNode result = objectMapper.readValue(json, JsonNode.class);
        String jsonToken = String.valueOf(result.get("token"));
        String token = jsonToken.substring(1, jsonToken.length() - 1);
        HttpSession session = request.getSession();
        session.setAttribute("jwt_token", token);
        if (token == null) {
            return new RedirectView("/login");
        }
        return new RedirectView("/main");
    }
    @PostMapping("/register")
    public RedirectView register(@ModelAttribute User user) {
        if (authenticationRestController.register(user).getStatusCode() == HttpStatus.BAD_REQUEST) {
            return new RedirectView("/register");
        }
        return new RedirectView("/login");
    }
    @PostMapping("/upload_file")
    public RedirectView uploadFile(@RequestParam("file") MultipartFile file, HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String token = String.valueOf(session.getAttribute("jwt_token"));
        FileBackup fileBackup = null;
        fileBackup = fileService.saveFile(file, token);
        String downloadURL = "";
        downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/")
                .path(fileBackup.getId()).toUriString();
        String loginedEmail = jwtTokenProvider.getUsername(token);
        ResponseFile response = new ResponseFile(fileBackup.getId(), loginedEmail, fileBackup.getFileName(),
                downloadURL, file.getContentType(), file.getSize());
        fileService.saveResponseFile(response);
        return new RedirectView("/main");
    }
}
