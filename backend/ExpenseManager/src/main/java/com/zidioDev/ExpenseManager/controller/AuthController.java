package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.RegisterRequest;
import com.zidioDev.ExpenseManager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        try {
            logger.info("Attempting to register user with email: {}", request.getEmail());
            AuthResponse response = authService.register(request);
            logger.info("Successfully registered user with email: {}", request.getEmail());
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            logger.error("Registration failed due to invalid argument: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Registration Failed: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during registration: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        try {
            logger.info("Attempting login for user: {}", request.getEmail());
            AuthResponse response = authService.login(request);
            logger.info("Successfully logged in user: {}", request.getEmail());
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Login failed due to invalid credentials: {}", e.getMessage(), e);
            return ResponseEntity.status(401).body("Invalid credentials: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Unexpected error during login: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().body("Login failed: " + e.getMessage());
        }
    }
}


