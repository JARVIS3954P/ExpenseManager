package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.AuthDTO.*;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse register(RegisterRequest request);
}
