package com.zidio.ExpenseManager.config;

import com.zidio.ExpenseManager.enums.UserRole;
import com.zidio.ExpenseManager.model.User;
import com.zidio.ExpenseManager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // This code will run on application startup

        // Check if users already exist. If so, don't seed again.
        if (userRepository.count() == 0) {
            System.out.println("No users found. Seeding initial data...");

            // 1. Create the Admin User
            User admin = User.builder()
                    .fullName("Admin User")
                    .email("admin@zidio.com")
                    .password(passwordEncoder.encode("admin123"))
                    .role(UserRole.ADMIN)
                    .manager(null) // Admins might not have a manager
                    .build();
            userRepository.save(admin);

            // 2. Create the Manager User
            // We assign the Admin as the manager for this Manager
            User manager = User.builder()
                    .fullName("Manager User")
                    .email("manager@zidio.com")
                    .password(passwordEncoder.encode("manager123"))
                    .role(UserRole.MANAGER)
                    .manager(admin)
                    .build();
            userRepository.save(manager);

            // 3. Create the Employee User
            // We assign the Manager as the manager for this Employee
            User employee = User.builder()
                    .fullName("Employee User")
                    .email("employee@zidio.com")
                    .password(passwordEncoder.encode("employee123"))
                    .role(UserRole.EMPLOYEE)
                    .manager(manager)
                    .build();
            userRepository.save(employee);

            System.out.println("Initial user data has been seeded.");
            System.out.println("----------------------------------------");
            System.out.println("Test Credentials:");
            System.out.println("Admin:    admin@zidio.com / admin123");
            System.out.println("Manager:  manager@zidio.com / manager123");
            System.out.println("Employee: employee@zidio.com / employee123");
            System.out.println("----------------------------------------");
        } else {
            System.out.println("Users already exist in the database. Skipping data seeding.");
        }
    }
}