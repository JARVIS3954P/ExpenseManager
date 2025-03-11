package com.zidioDev.ExpenseManager.config;

import com.google.common.util.concurrent.RateLimiter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class RateLimitFilter extends OncePerRequestFilter {
    
    // Create a rate limiter that allows 5 requests per second for auth endpoints
    private final RateLimiter authRateLimiter = RateLimiter.create(5.0);
    
    // Create a rate limiter that allows 20 requests per second for other endpoints
    private final RateLimiter defaultRateLimiter = RateLimiter.create(20.0);

    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getRequestURI();
        
        // Apply stricter rate limiting for authentication endpoints
        if (path.startsWith("/api/auth/")) {
            if (!authRateLimiter.tryAcquire()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
                response.getWriter().write("Too many authentication requests. Please try again later.");
                return;
            }
        } 
        // Apply default rate limiting for other endpoints
        else if (!defaultRateLimiter.tryAcquire()) {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
            return;
        }

        filterChain.doFilter(request, response);
    }
} 