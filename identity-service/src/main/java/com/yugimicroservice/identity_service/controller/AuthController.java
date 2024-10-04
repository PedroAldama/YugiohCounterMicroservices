package com.yugimicroservice.identity_service.controller;

import com.yugimicroservice.identity_service.dto.AuthRequestDto;
import com.yugimicroservice.identity_service.entity.UserCredential;
import com.yugimicroservice.identity_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController{

    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredential user){
        return service.saveUser(user);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequestDto authRequestDto){
       Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequestDto.getUsername(), authRequestDto.getPassword()
        ));
       if(auth.isAuthenticated()){
            return service.generateToken(authRequestDto.getUsername());
       }else{
           throw new RuntimeException("Authentication failed");
       }

    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token){
         service.validateToken(token);
         return "Token is valid";
    }
}
