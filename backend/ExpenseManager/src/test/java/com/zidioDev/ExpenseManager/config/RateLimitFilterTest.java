package com.zidioDev.ExpenseManager.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

import static org.mockito.Mockito.*;

class RateLimitFilterTest {

    private RateLimitFilter rateLimitFilter;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        rateLimitFilter = new RateLimitFilter();
    }

    @Test
    void testAuthEndpointRateLimit() throws Exception {
        // Setup
        when(request.getRequestURI()).thenReturn("/api/auth/login");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Send requests in a burst
        int passedRequests = 0;
        for (int i = 0; i < 10; i++) {
            rateLimitFilter.doFilterInternal(request, response, filterChain);
            if (stringWriter.toString().isEmpty()) {
                passedRequests++;
            }
            stringWriter.getBuffer().setLength(0); // Clear the buffer for next request
        }

        // Verify that some requests were rate limited
        verify(filterChain, atMost(6)).doFilter(request, response);
        verify(response, atLeast(1)).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }

    @Test
    void testNonAuthEndpointRateLimit() throws Exception {
        // Setup
        when(request.getRequestURI()).thenReturn("/api/expenses");
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        // Send requests in a burst
        int passedRequests = 0;
        for (int i = 0; i < 25; i++) {
            rateLimitFilter.doFilterInternal(request, response, filterChain);
            if (stringWriter.toString().isEmpty()) {
                passedRequests++;
            }
            stringWriter.getBuffer().setLength(0); // Clear the buffer for next request
        }

        // Verify that some requests were rate limited
        verify(filterChain, atMost(21)).doFilter(request, response);
        verify(response, atLeast(1)).setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
    }
} 