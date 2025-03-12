package com.zidioDev.ExpenseManager.controller;

import com.zidioDev.ExpenseManager.dto.UserDTO;
import com.zidioDev.ExpenseManager.dto.UserRequestDTO;
import com.zidioDev.ExpenseManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Get all users with optional pagination
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userService.getAllUsers(page, size));
    }

    // Get a specific user by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // Get current user profile
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    // Create a new user - admin only
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody UserRequestDTO userRequest) {
        return new ResponseEntity<>(userService.createUser(userRequest), HttpStatus.CREATED);
    }

    // Update an existing user by ID
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @securityUtils.isCurrentUser(#id)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO userRequest) {
        return ResponseEntity.ok(userService.updateUser(id, userRequest));
    }

    // Soft delete a specific user by ID
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully");
    }
}

