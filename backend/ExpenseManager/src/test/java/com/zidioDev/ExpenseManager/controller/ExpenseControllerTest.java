package com.zidioDev.ExpenseManager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zidioDev.ExpenseManager.ExpenseManagerApplication;
import com.zidioDev.ExpenseManager.config.TestSecurityConfig;
import com.zidioDev.ExpenseManager.dto.ExpenseDTO;
import com.zidioDev.ExpenseManager.model.enums.ExpenseCategory;
import com.zidioDev.ExpenseManager.service.ExpenseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ExpenseController.class)
@Import({TestSecurityConfig.class, ExpenseManagerApplication.class})
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ExpenseService expenseService;

    private ExpenseDTO employeeExpense;
    private ExpenseDTO otherUserExpense;

    @BeforeEach
    void setUp() {
        employeeExpense = ExpenseDTO.builder()
                .title("Test Expense")
                .description("Test Expense Description")
                .amount(100.00)
                .date(LocalDateTime.now())
                .category(ExpenseCategory.FOOD)
                .userId(2L) // Employee user ID
                .build();

        otherUserExpense = ExpenseDTO.builder()
                .title("Other User Expense")
                .description("Other User Expense Description")
                .amount(200.00)
                .date(LocalDateTime.now())
                .category(ExpenseCategory.TRAVEL)
                .userId(3L) // Different user ID
                .build();
    }

    // Employee Role Tests
    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void createExpense_Success() throws Exception {
        when(expenseService.createExpense(any(ExpenseDTO.class)))
                .thenReturn(employeeExpense);

        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeExpense)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void getMyExpenses_Success() throws Exception {
        when(expenseService.getExpensesByUser(eq(2L)))
                .thenReturn(Arrays.asList(employeeExpense));

        mockMvc.perform(get("/api/expenses"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void getExpenseById_OwnExpense_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(employeeExpense);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void getExpenseById_OtherUserExpense_Forbidden() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void updateExpense_OwnExpense_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(employeeExpense);
        when(expenseService.updateExpense(eq(1L), any(ExpenseDTO.class)))
                .thenReturn(employeeExpense);

        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeExpense)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void updateExpense_OtherUserExpense_Forbidden() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherUserExpense)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void deleteExpense_OwnExpense_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(employeeExpense);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "employee@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void deleteExpense_OtherUserExpense_Forbidden() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isForbidden());
    }

    // Manager Role Tests
    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_getExpensesByUser_Success() throws Exception {
        when(expenseService.getExpensesByUser(eq(2L)))
                .thenReturn(Arrays.asList(employeeExpense));

        mockMvc.perform(get("/api/expenses/user/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_getExpenseById_AnyUser_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(get("/api/expenses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_updateExpense_AnyUser_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);
        when(expenseService.updateExpense(eq(1L), any(ExpenseDTO.class)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(put("/api/expenses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(otherUserExpense)))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_deleteExpense_AnyUser_Success() throws Exception {
        when(expenseService.getExpenseById(eq(1L)))
                .thenReturn(otherUserExpense);

        mockMvc.perform(delete("/api/expenses/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "test@example.com", userDetailsServiceBeanName = "testUserDetailsService")
    void manager_createExpense_Forbidden() throws Exception {
        mockMvc.perform(post("/api/expenses")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employeeExpense)))
                .andExpect(status().isForbidden());
    }

    @Test
    void contextLoads() {
    }
} 