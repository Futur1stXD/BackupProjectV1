package com.example.backupproject.security.jwt;

import com.example.backupproject.model.Status;
import com.example.backupproject.model.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public final class JwtUserFactory {
    public JwtUserFactory() {}
    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                Collections.singleton(new SimpleGrantedAuthority(user.getRole().name()))
        );
    }
}