package com.yugimicroservice.identity_service.service;

import com.yugimicroservice.identity_service.entity.UserCredential;
import com.yugimicroservice.identity_service.repository.UserCredentialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JWTService jwtService;

    public String saveUser(UserCredential user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        repository.save(user);
        return "Saved";
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
         jwtService.validateToken(token);
    }
}
