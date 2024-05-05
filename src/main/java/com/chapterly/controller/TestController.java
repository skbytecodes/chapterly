package com.chapterly.controller;

import com.chapterly.dto.AuthenticationRequest;
import com.chapterly.dto.AuthenticationResponse;
import com.chapterly.dto.RegisterRequest;
import com.chapterly.security.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/v1")
public class TestController {

    @Autowired
    private AuthenticationService service;
    @GetMapping("/secured")
    public String secured(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        System.out.println("Username: " + username);
        return "Hello from secured endpoint";
    }

    @GetMapping("/open")
    public String open(){
        return "Hello from open endpoint";
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }
}
