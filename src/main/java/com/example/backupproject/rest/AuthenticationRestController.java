package com.example.backupproject.rest;

import com.example.backupproject.dto.AuthenticationRequestDto;
import com.example.backupproject.model.User;
import com.example.backupproject.security.jwt.JwtTokenProvider;
import com.example.backupproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationRestController {
    private JwtTokenProvider jwtTokenProvider;
    private UserService userService;
    private BCryptPasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationRestController(JwtTokenProvider jwtTokenProvider, UserService userService, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequestDto requestDto) {
        try {
            User user = userService.findByUsername(requestDto.getEmail());
            if (user == null) {
                return ResponseEntity.badRequest().body("User with email: " + requestDto.getEmail() + " not found!");
            }
            if (passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(requestDto.getEmail(),
                        requestDto.getPassword()));
                String token = jwtTokenProvider.createToken(requestDto.getEmail(), user.getRole().name());
                Map<Object, Object> response = new HashMap<>();
                response.put("email", requestDto.getEmail());
                response.put("token", token);
                return ResponseEntity.ok(response);
            }
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    }
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        User userCheck = userService.findByUsername(user.getEmail());
        if (userCheck != null) {
            return ResponseEntity.badRequest().body("This user with this email: " + user.getEmail() + " is already exist!");
        }
        userService.register(user);
        return ResponseEntity.ok().body(user);
    }
    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        securityContextLogoutHandler.logout(request, response, null);
    }
}