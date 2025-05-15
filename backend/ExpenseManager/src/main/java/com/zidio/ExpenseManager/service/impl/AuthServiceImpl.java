package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.auth.AuthRequest;
import com.zidio.ExpenseManager.dto.auth.AuthResponse;
import com.zidio.ExpenseManager.dto.auth.RegisterRequest;
import com.zidio.ExpenseManager.enums.UserRole;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.security.jwt.JwtService;
import com.zidio.ExpenseManager.service.interfaces.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponse register(RegisterRequest request) {
        User user = User.builder()
                .fullName(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : UserRole.EMPLOYEE)
                .build();

        userRepository.save(user);
        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        String token = jwtService.generateToken(user);
        return new AuthResponse(token);
    }
}
