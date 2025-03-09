package com.zidioDev.ExpenseManager.model;

import com.zidioDev.ExpenseManager.model.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Full name cannot be blank")
    @Column(nullable = false)
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Invalid email format")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Column(nullable = false)
    private String password; // Store hashed passwords only!

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role; // EMPLOYEE, MANAGER, ADMIN

    @Column(nullable = false)
    private boolean isDeleted = false; // Soft delete flag

}
