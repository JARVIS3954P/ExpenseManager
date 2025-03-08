package com.zidioDev.ExpenseManager.service.impl;

import com.zidioDev.ExpenseManager.dto.UserDTO;
import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.repository.UserRepository;
import com.zidioDev.ExpenseManager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getRole().name()))
                .collect(Collectors.toList());
    }
}

