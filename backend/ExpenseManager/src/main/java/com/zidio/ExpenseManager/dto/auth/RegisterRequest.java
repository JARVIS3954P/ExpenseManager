package com.zidio.ExpenseManager.dto.auth;

import com.zidio.ExpenseManager.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private UserRole role;
}
