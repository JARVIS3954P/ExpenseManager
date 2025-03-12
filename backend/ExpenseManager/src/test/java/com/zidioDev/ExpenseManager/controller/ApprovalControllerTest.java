package com.zidioDev.ExpenseManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.ExpenseManagerApplication;
import com.zidioDev.ExpenseManager.config.TestSecurityConfig;
import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ApprovalController.class)
@Import({TestSecurityConfig.class, ExpenseManagerApplication.class})
class ApprovalControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApprovalService approvalService;

    private ApprovalDTO approvalDTO;

    @BeforeEach
    void setUp() {
        approvalDTO = ApprovalDTO.builder()
                .expenseId(1L)
                .approverRole("MANAGER")
                .approved(true)
                .build();
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void approveExpense_Success() throws Exception {
        when(approvalService.approveExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/approve/{expenseId}", 1L)
                .param("approverRole", "MANAGER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void rejectExpense_Success() throws Exception {
        approvalDTO.setApproved(false);
        approvalDTO.setRejectionReason("Test rejection reason");
        when(approvalService.rejectExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/reject/{expenseId}", 1L)
                .param("approverRole", "MANAGER")
                .param("rejectionReason", "Test rejection reason")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void contextLoads() {
    }
} 