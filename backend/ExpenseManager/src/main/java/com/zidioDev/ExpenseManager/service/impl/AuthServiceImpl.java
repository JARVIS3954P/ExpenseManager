package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.config.JwtUtils;
import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.AuthDTO.RegisterRequest;
import com.zidioDev.ExpenseManager.dto.UserDTO;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtService;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            logger.debug("Attempting to find user by email: {}", request.getEmail());
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            logger.debug("User found, verifying password");

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                logger.debug("Password verification failed");
                throw new BadCredentialsException("Invalid password");
            }
            logger.debug("Password verified successfully");

            String token = jwtService.generateToken(user);
            logger.debug("JWT token generated successfully");
            
            return new AuthResponse(token, user.getRole().name(), "Login successful");
        } catch (Exception e) {
            logger.error("Error during login process", e);
            throw e;
        }
    }

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {
        try {
            logger.debug("Starting registration process for email: {}", request.getEmail());
            
            logger.debug("Checking if email already exists");
            if (userRepository.existsByEmail(request.getEmail())) {
                logger.debug("Email already exists: {}", request.getEmail());
                throw new RuntimeException("Email already exists");
            }
            
            logger.debug("Creating new user entity");
            User user = new User();
            user.setFullName(request.getName());
            user.setEmail(request.getEmail());
            String encodedPassword = passwordEncoder.encode(request.getPassword());
            logger.debug("Password encoded successfully");
            user.setPassword(encodedPassword);
            user.setRole(request.getRole());
            logger.debug("User entity created with role: {}", request.getRole());

            logger.debug("Saving user to database");
            user = userRepository.save(user);
            logger.debug("User saved successfully with ID: {}", user.getId());

            logger.debug("Generating JWT token");
            String token = jwtService.generateToken(user);
            logger.debug("JWT token generated successfully");

            return new AuthResponse(token, user.getRole().name(), "Registration successful");
        } catch (Exception e) {
            logger.error("Error during registration process", e);
            throw e;
        }
    }
}

