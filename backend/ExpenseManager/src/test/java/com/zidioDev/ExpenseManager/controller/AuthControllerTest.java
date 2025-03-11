package com.zidioDev.ExpenseManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.config.TestSecurityConfig;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.AuthDTO.RegisterRequest;
import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(TestSecurityConfig.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;
    private String uniqueEmail;

    @BeforeEach
    void setUp() {
        uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        registerRequest = new RegisterRequest();
        registerRequest.setName("Test User");
        registerRequest.setEmail(uniqueEmail);
        registerRequest.setPassword("password123"); // Use raw password for registration
        registerRequest.setRole(Role.EMPLOYEE);

        loginRequest = new LoginRequest();
        loginRequest.setEmail(uniqueEmail);
        loginRequest.setPassword("password123"); // Use raw password for login

        // Mock authentication service responses
        AuthResponse mockResponse = new AuthResponse("mock-jwt-token", "EMPLOYEE", "Success");

        when(authService.register(any(RegisterRequest.class))).thenReturn(mockResponse);
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);
    }

    @Test
    void registerUser_Success() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());
    }

    @Test
    void loginUser_Success() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());
    }
}