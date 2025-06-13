package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody Map<String, String> request) {
        User user = authService.register(
            request.get("firstName"),
            request.get("lastName"),
            request.get("email"),
            request.get("password")
        );
        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<Boolean> authenticate(@RequestBody Map<String, String> request) {
        boolean isAuthenticated = authService.authenticate(
            request.get("email"),
            request.get("password")
        );
        return ResponseEntity.ok(isAuthenticated);
    }
} 