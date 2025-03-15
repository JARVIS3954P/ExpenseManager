package com.zidioDev.ExpenseManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.ExpenseManagerApplication;
import com.zidioDev.ExpenseManager.config.TestSecurityConfig;
import com.zidioDev.ExpenseManager.dto.expense.ApprovalDTO;
import com.zidioDev.ExpenseManager.model.enums.ApprovalStatus;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    // Manager Role Tests
    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_approveExpense_Success() throws Exception {
        when(approvalService.approveExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/approve/{expenseId}", 1L)
                .param("approverRole", "MANAGER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true));
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_rejectExpense_Success() throws Exception {
        approvalDTO.setApproved(false);
        approvalDTO.setRejectionReason("Budget constraints");
        when(approvalService.rejectExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/reject/{expenseId}", 1L)
                .param("approverRole", "MANAGER")
                .param("rejectionReason", "Budget constraints")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.rejectionReason").value("Budget constraints"));
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_getPendingApprovals_Success() throws Exception {
        when(approvalService.getPendingApprovals("MANAGER"))
                .thenReturn(java.util.Collections.singletonList(approvalDTO));

        mockMvc.perform(get("/api/approval/pending")
                .param("role", "MANAGER")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Admin Role Tests
    @Test
    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void admin_approveExpense_Success() throws Exception {
        approvalDTO.setApproverRole("ADMIN");
        when(approvalService.approveExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/approve/{expenseId}", 1L)
                .param("approverRole", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(true))
                .andExpect(jsonPath("$.approverRole").value("ADMIN"));
    }

    @Test
    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void admin_rejectExpense_Success() throws Exception {
        approvalDTO.setApproved(false);
        approvalDTO.setApproverRole("ADMIN");
        approvalDTO.setRejectionReason("Policy violation");
        when(approvalService.rejectExpense(any(ApprovalDTO.class)))
                .thenReturn(approvalDTO);

        mockMvc.perform(post("/api/approval/reject/{expenseId}", 1L)
                .param("approverRole", "ADMIN")
                .param("rejectionReason", "Policy violation")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.approved").value(false))
                .andExpect(jsonPath("$.approverRole").value("ADMIN"))
                .andExpect(jsonPath("$.rejectionReason").value("Policy violation"));
    }

    @Test
    @WithUserDetails(value = "admin@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void admin_getPendingApprovals_Success() throws Exception {
        when(approvalService.getPendingApprovals("ADMIN"))
                .thenReturn(java.util.Collections.singletonList(approvalDTO));

        mockMvc.perform(get("/api/approval/pending")
                .param("role", "ADMIN")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    // Access Control Tests
    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void employee_approveExpense_Forbidden() throws Exception {
        mockMvc.perform(post("/api/approval/approve/{expenseId}", 1L)
                .param("approverRole", "EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void employee_getPendingApprovals_Forbidden() throws Exception {
        mockMvc.perform(get("/api/approval/pending")
                .param("role", "EMPLOYEE")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    void contextLoads() {
    }
}