package com.zidio.ExpenseManager.service.impl;

import com.zidio.ExpenseManager.dto.ExpenseSummaryDTO;
import com.zidio.ExpenseManager.dto.TeamMemberExpenseDTO;
import com.zidio.ExpenseManager.enums.ExpenseStatus;
import com.zidio.ExpenseManager.enums.UserRole;
import com.zidio.ExpenseManager.model.Expense;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.ExpenseRepository;
import com.zidio.ExpenseManager.repository.UserRepository;
import com.zidio.ExpenseManager.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    @Override
    public List<ExpenseSummaryDTO> getExpenseSummaryByCategory(){
        return expenseRepository.getExpenseSummaryByCategory();
    }

    @Override
    public Map<String, Object> getDashboardSummary() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserEmail = authentication.getName();
        User user = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Map<String, Object> summary = new HashMap<>();

        if (user.getRole() == UserRole.ADMIN) {
            summary.put("totalUsers", userRepository.count());
            summary.put("pendingApprovals", expenseRepository.countByStatusIn(Arrays.asList(
                    ExpenseStatus.PENDING_MANAGER_APPROVAL,
                    ExpenseStatus.PENDING_ADMIN_APPROVAL,
                    ExpenseStatus.PENDING_FINANCE_APPROVAL // Added for future-proofing
            )));
            summary.put("totalExpensesValue", expenseRepository.findAll().stream()
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        } else if (user.getRole() == UserRole.MANAGER) {
            summary.put("teamSize", userRepository.findAll().stream()
                    .filter(u -> u.getManager() != null && u.getManager().getId().equals(user.getId()))
                    .count());
            summary.put("pendingApprovals", expenseRepository.findByCurrentApproverId(user.getId()).size());
        } else { // EMPLOYEE
            List<Expense> myExpenses = expenseRepository.findByUserId(user.getId());
            summary.put("myPendingExpenses", myExpenses.stream()
                    .filter(e -> e.getStatus().toString().startsWith("PENDING")).count());
            summary.put("myApprovedExpenses", myExpenses.stream()
                    .filter(e -> e.getStatus() == ExpenseStatus.APPROVED).count());
            summary.put("spentThisMonth", myExpenses.stream()
                    .filter(e -> e.getExpenseDate().getMonth() == java.time.LocalDate.now().getMonth() && e.getExpenseDate().getYear() == java.time.LocalDate.now().getYear())
                    .map(Expense::getAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));
        }
        return summary;
    }

    @Override
    public List<TeamMemberExpenseDTO> getTeamExpenseSummary() {
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("Current user not found"));

        User manager = currentUser.getRole() == UserRole.MANAGER ? currentUser : currentUser.getManager();

        if (manager == null) {
            return Collections.emptyList(); // This user is not a manager and has no manager
        }

        return expenseRepository.getTeamExpenseSummary(manager.getId());
    }
}
