package com.zidioDev.ExpenseManager.config;

import com.zidioDev.ExpenseManager.model.User;
import com.zidioDev.ExpenseManager.model.UserPrincipal;
import com.zidioDev.ExpenseManager.model.enums.Role;
import com.zidioDev.ExpenseManager.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

@TestConfiguration
@EnableWebSecurity
public class TestSecurityConfig {

    @Bean
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http, 
                                                     JwtAuthenticationFilter jwtAuthenticationFilter,
                                                     RateLimitFilter rateLimitFilter) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentSecurityPolicy(csp -> csp.policyDirectives("default-src 'self'; frame-ancestors 'none';"))
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/expenses/**").authenticated()
                .requestMatchers("/api/approval/**").hasAnyRole("MANAGER", "ADMIN")
                .requestMatchers("/api/reports/**").hasRole("ADMIN")
                .requestMatchers("/docs/**", "/v3/api-docs/**", "/swagger-ui/**", "/ws/**").permitAll()
                .anyRequest().authenticated()
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setStatus(401);
                    response.getWriter().write("Unauthorized");
                })
            )
            .authenticationProvider(testAuthenticationProvider())
            .addFilterBefore(rateLimitFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "X-Requested-With"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Primary
    public UserDetailsService testUserDetailsService() {
        return username -> {
            if ("test@example.com".equals(username)) {
                User user = User.builder()
                        .id(1L)
                        .email("test@example.com")
                        .password(testPasswordEncoder().encode("password"))
                        .role(Role.MANAGER)
                        .build();
                return new UserPrincipal(user);
            } else if ("employee@example.com".equals(username)) {
                User user = User.builder()
                        .id(2L)
                        .email("employee@example.com")
                        .password(testPasswordEncoder().encode("password"))
                        .role(Role.EMPLOYEE)
                        .build();
                return new UserPrincipal(user);
            } else if ("admin@example.com".equals(username)) {
                User user = User.builder()
                        .id(3L)
                        .email("admin@example.com")
                        .password(testPasswordEncoder().encode("password"))
                        .role(Role.ADMIN)
                        .build();
                return new UserPrincipal(user);
            }
            throw new UsernameNotFoundException("User not found: " + username);
        };
    }

    @Bean
    @Primary
    public PasswordEncoder testPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @Primary
    public AuthenticationProvider testAuthenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(testUserDetailsService());
        authProvider.setPasswordEncoder(testPasswordEncoder());
        return authProvider;
    }

    @Bean
    @Primary
    public AuthenticationManager testAuthenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    @Primary
    public JwtUtils testJwtUtils() {
        JwtUtils jwtUtils = Mockito.mock(JwtUtils.class);
        Mockito.when(jwtUtils.validateToken(Mockito.anyString())).thenReturn(true);
        Mockito.when(jwtUtils.extractUsername(Mockito.anyString())).thenReturn("test@example.com");
        return jwtUtils;
    }

    @Bean
    @Primary
    public JwtAuthenticationFilter testJwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(testJwtUtils(), testUserDetailsService());
    }

    @Bean
    @Primary
    public RateLimitFilter testRateLimitFilter() {
        return new RateLimitFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) 
                    throws ServletException, IOException {
                filterChain.doFilter(request, response);
            }
        };
    }
} 