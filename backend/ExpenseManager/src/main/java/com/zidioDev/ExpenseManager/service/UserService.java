package com.zidioDev.ExpenseManager.service;

import com.zidioDev.ExpenseManager.dto.UserDTO;

import java.util.List;

public interface UserService {
    UserDTO getUserById(Long userId);
    List<UserDTO> getAllUsers();
}

