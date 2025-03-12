package com.zidioDev.ExpenseManager.config;

import com.zidioDev.ExpenseManager.ExpenseManagerApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = {ExpenseManagerApplication.class, SecurityConfig.class})
class SecurityConfigTest {

    @Test
    void contextLoads() {
    }

} 