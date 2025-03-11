package com.zidioDev.ExpenseManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.config.TestSecurityConfig;
import com.zidioDev.ExpenseManager.dto.ApprovalDTO;
import com.zidioDev.ExpenseManager.service.ApprovalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
public class ApprovalControllerTest {

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
                .reason("Test rejection reason")
                .build();
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void approveExpense_Success() throws Exception {
        when(approvalService.approveExpense(eq(1L), any(String.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approvals/1/approve"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void rejectExpense_Success() throws Exception {
        when(approvalService.rejectExpense(eq(1L), any(String.class), any(String.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approvals/1/reject")
                .param("reason", "Test rejection reason"))
                .andExpect(status().isOk());
    }
} 