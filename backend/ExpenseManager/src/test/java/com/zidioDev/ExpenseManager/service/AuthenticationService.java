package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.AuthDTO.RegisterRequest;

public interface AuthenticationService {
    String register(RegisterRequest request);
    String authenticate(LoginRequest request);
} 