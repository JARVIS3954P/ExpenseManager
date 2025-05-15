package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.dto.auth.AuthRequest;
import com.zidio.ExpenseManager.dto.auth.AuthResponse;
import com.zidio.ExpenseManager.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse authenticate(AuthRequest request);
}
