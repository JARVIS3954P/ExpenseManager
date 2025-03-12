package com.zidioDev.ExpenseManager.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "expense.approval")
public class ApprovalConfig {
    private Double autoApprovalThreshold;
    private Double managerApprovalThreshold;
    // Above managerApprovalThreshold requires admin approval
} 

