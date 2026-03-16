package com.smartboard.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Lightweight sliding-window rate limiter for the login endpoint.
 *
 * Limits each client IP to {@code app.rate-limit.login-max-attempts} POST requests
 * to {@code /auth/login} within a {@code app.rate-limit.login-window-ms} millisecond window.
 * Exceeding the limit returns HTTP 429.
 *
 * NOTE: This in-memory implementation is suitable for single-instance deployments.
 * For a horizontally-scaled production environment, replace with a distributed
 * counter (e.g. Bucket4j + Redis).
 */
@Slf4j
@Component
@Order(1)   // Run before Spring Security's filter chain
public class LoginRateLimitFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/auth/login";

    @Value("${app.rate-limit.login-max-attempts}")
    private int maxAttempts;

    @Value("${app.rate-limit.login-window-ms}")
    private long windowMs;

    // ip → attempt count within current window
    private final ConcurrentHashMap<String, AtomicInteger> attemptCounts = new ConcurrentHashMap<>();
    // ip → window start timestamp
    private final ConcurrentHashMap<String, Long> windowStarts = new ConcurrentHashMap<>();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {

        if (!isLoginRequest(request)) {
            chain.doFilter(request, response);
            return;
        }

        String ip = resolveClientIp(request);
        long now = System.currentTimeMillis();

        // Reset window if expired
        windowStarts.compute(ip, (key, windowStart) -> {
            if (windowStart == null || now - windowStart > windowMs) {
                attemptCounts.put(key, new AtomicInteger(0));
                return now;
            }
            return windowStart;
        });

        int attempts = attemptCounts
                .computeIfAbsent(ip, k -> new AtomicInteger(0))
                .incrementAndGet();

        if (attempts > maxAttempts) {
            log.warn("Rate limit exceeded for IP {} on {} — {} attempts in window", ip, LOGIN_PATH, attempts);
            rejectWithTooManyRequests(response);
            return;
        }

        chain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                && LOGIN_PATH.equals(request.getRequestURI());
    }

    /**
     * Prefer the leftmost IP in X-Forwarded-For (set by load balancers / reverse proxies).
     * Falls back to the direct remote address when no proxy header is present.
     */
    private String resolveClientIp(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }

    private void rejectWithTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(429);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"error\":\"Too many login attempts. Please wait before trying again.\"," +
                "\"status\":429}"
        );
    }
}
