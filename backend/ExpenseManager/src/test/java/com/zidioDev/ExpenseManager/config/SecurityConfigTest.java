package com.zidioDev.ExpenseManager.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.controller.ApprovalController;
import com.zidioDev.ExpenseManager.controller.AuthController;
import com.zidioDev.ExpenseManager.controller.ExpenseController;
import com.zidioDev.ExpenseManager.controller.ReportController;
import com.zidioDev.ExpenseManager.dto.AuthDTO.AuthResponse;
import com.zidioDev.ExpenseManager.dto.AuthDTO.LoginRequest;
import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.dto.ApprovalDTO;
import com.zidioDev.ExpenseManager.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {
    AuthController.class,
    ExpenseController.class,
    ApprovalController.class,
    ReportController.class
})
@Import(TestSecurityConfig.class)
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private ExpenseService expenseService;

    @MockBean
    private ApprovalService approvalService;

    @MockBean
    private ReportService reportService;

    @MockBean
    private UserService userService;

    @MockBean
    private NotificationService notificationService;

    private String mockJwtToken;

    @BeforeEach
    void setUp() {
        mockJwtToken = "mock-jwt-token";
        AuthResponse mockResponse = new AuthResponse();
        mockResponse.setToken(mockJwtToken);
        mockResponse.setRole("ROLE_MANAGER");
        when(authService.login(any(LoginRequest.class))).thenReturn(mockResponse);

        // Mock expense service response
        ExpenseDTO mockExpense = new ExpenseDTO();
        mockExpense.setId(1L);
        mockExpense.setTitle("Test Expense");
        mockExpense.setAmount(100.0);
        when(expenseService.getExpenseById(any(Long.class))).thenReturn(mockExpense);

        // Mock approval service response
        ApprovalDTO mockApproval = new ApprovalDTO();
        when(approvalService.approveExpense(any(Long.class), any(String.class))).thenReturn(mockApproval);
    }

    @Test
    void testCorsConfiguration() throws Exception {
        mockMvc.perform(options("/api/auth/login")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_METHOD, "POST")
                .header(HttpHeaders.ACCESS_CONTROL_REQUEST_HEADERS, "content-type")
                .header(HttpHeaders.ORIGIN, "http://localhost:3000"))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN))
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS))
                .andExpect(header().exists(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS));
    }

    @Test
    void testSecurityEndpoints() throws Exception {
        // Test public endpoint (login)
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("test@example.com");
        loginRequest.setPassword("password");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk());

        // Test protected endpoints without authentication
        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/approvals/1"))
                .andExpect(status().isUnauthorized());

        mockMvc.perform(get("/api/reports/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "test@example.com", roles = "MANAGER")
    void testAuthenticatedEndpoints() throws Exception {
        // Test protected endpoints with authentication
        mockMvc.perform(get("/api/expenses/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockJwtToken))
                .andExpect(status().isOk());

        mockMvc.perform(post("/api/approvals/1/approve")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockJwtToken))
                .andExpect(status().isOk());

        // Test endpoint requiring ADMIN role (should be forbidden for MANAGER)
        mockMvc.perform(get("/api/reports/1")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockJwtToken))
                .andExpect(status().isForbidden());
    }
} 