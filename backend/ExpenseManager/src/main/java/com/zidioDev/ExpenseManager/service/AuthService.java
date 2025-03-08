package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.AuthDTO.*;
import com.zidioDev.ExpenseManager.dto.UserDTO;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    UserDTO register(RegisterRequest request);
}
