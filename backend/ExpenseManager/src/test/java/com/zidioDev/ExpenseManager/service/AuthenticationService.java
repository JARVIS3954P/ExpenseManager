package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.AuthDTO.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
} 