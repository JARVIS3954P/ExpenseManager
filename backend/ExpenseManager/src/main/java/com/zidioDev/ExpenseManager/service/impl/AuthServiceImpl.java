package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.config.JwtUtils;
import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.RegisterRequest;
import com.zidioDev.ExpenseManager.exception.BadRequestException;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    public AuthResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new BadRequestException("User not found"));

            String jwt = jwtUtils.generateToken(user);

            return AuthResponse.builder()
                    .id(user.getId())
                    .email(user.getEmail())
                    .token(jwt)
                    .role(user.getRole().name())
                    .message("Login successful")
                    .build();
        } catch (Exception e) {
            throw new BadRequestException("Invalid email/password");
        }
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email already exists");
        }

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.valueOf(request.getRole()))
                .isDeleted(false)
                .build();

        User savedUser = userRepository.save(user);

        String jwt = jwtUtils.generateToken(user);

        return AuthResponse.builder()
                .id(savedUser.getId())
                .email(savedUser.getEmail())
                .token(jwt)
                .role(savedUser.getRole().name())
                .message("Registration successful")
                .build();
    }
}



