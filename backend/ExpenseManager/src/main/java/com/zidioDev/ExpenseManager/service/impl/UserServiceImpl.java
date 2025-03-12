package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.UserDTO;
import com.zidioDev.ExpenseManager.dto.UserRequestDTO;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Don't return deleted users
        if (user.isDeleted()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        return mapToDto(user);
    }

    @Override
    public UserDTO getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        return mapToDto(user);
    }

    @Override
    public List<UserDTO> getAllUsers(int page, int size) {
        return userRepository.findAllActiveUsers(PageRequest.of(page, size))
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO createUser(UserRequestDTO userRequest) {
        // Check if email already exists in the database
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Create new user entity from request
        User user = User.builder()
                .fullName(userRequest.getFullName())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .role(userRequest.getRole())
                .isDeleted(false)
                .build();

        // Save entity and return mapped DTO
        return mapToDto(userRepository.save(user));
    }

    @Override
    public UserDTO updateUser(Long userId, UserRequestDTO userRequest) {
        // Fetch existing user by ID and throw exception if not found
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (existingUser.isDeleted()) {
            throw new RuntimeException("User not found with id: " + userId);
        }

        // Check if the new email is already used by another user
        if (!existingUser.getEmail().equals(userRequest.getEmail()) &&
                userRepository.existsByEmail(userRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        // Update fields from input object to existing entity
        existingUser.setFullName(userRequest.getFullName());
        existingUser.setEmail(userRequest.getEmail());

        // Only update password if provided
        if (userRequest.getPassword() != null && !userRequest.getPassword().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        }

        existingUser.setRole(userRequest.getRole());

        // Save updated entity and return mapped DTO
        return mapToDto(userRepository.save(existingUser));
    }

    @Override
    public void deleteUser(Long userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        // Perform soft delete by setting isDeleted flag to true
        existingUser.setDeleted(true);

        // Save updated entity back to database
        userRepository.save(existingUser);
    }

    /**
     * Helper method to map a User entity to a UserDTO.
     */
    private UserDTO mapToDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name()
        );
    }
}

