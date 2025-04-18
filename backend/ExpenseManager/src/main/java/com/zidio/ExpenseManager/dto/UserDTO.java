package com.zidio.ExpenseManager.dto;

import com.zidio.ExpenseManager.enums.UserRole;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private UserRole role;
}
