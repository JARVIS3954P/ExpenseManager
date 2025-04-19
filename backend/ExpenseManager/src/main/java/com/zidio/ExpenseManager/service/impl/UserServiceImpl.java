package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.UserDTO;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.service.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private User mapToEntity(UserDTO dto) {
        return User.builder()
                .id(dto.getId())
                .fullName(dto.getUsername())
                .email(dto.getEmail())
                .role(dto.getRole())
                .build();
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        User user = userRepository.save(mapToEntity(userDTO));
        return mapToDTO(user);
    }

    @Override
    public UserDTO getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public UserDTO updateUser(UserDTO userDTO) {
        // Check if user exists
        if (!userRepository.existsById(userDTO.getId())) {
            throw new RuntimeException("User not found");
        }
        User user = userRepository.save(mapToEntity(userDTO));
        return mapToDTO(user);
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public UserDTO getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }
}