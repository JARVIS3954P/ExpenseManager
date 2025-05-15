package com.zidio.ExpenseManager.service.interfaces;

import com.zidio.ExpenseManager.dto.UserDTO;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface UserService {
    UserDTO createUser(UserDTO userDTO);
    UserDTO getUserById(Long id);
    List<UserDTO> getAllUsers();
    UserDTO updateUser(UserDTO userDTO);
    void deleteUser(Long id);
    UserDTO getUserByEmail(String email);
    UserDetails loadUserByUsername(String email);
}