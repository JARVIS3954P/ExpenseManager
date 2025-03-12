package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.RegisterRequest;

public interface AuthenticationService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
} 