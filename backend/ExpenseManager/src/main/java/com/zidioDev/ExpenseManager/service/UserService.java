package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.UserDTO;
import com.zidioDev.ExpenseManager.dto.UserRequestDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);
    UserDTO getCurrentUser();
    List<UserDTO> getAllUsers(int page, int size);
    UserDTO createUser(UserRequestDTO userRequest);
    UserDTO updateUser(Long userId, UserRequestDTO userRequest);
    void deleteUser(Long userId); // Soft delete
}

