package com.mariuszilinskas.streamix.security.filter;

import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@NullMarked
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String providedKey = request.getHeader(API_KEY_HEADER);

        if (!isValidApiKey(providedKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidApiKey(String providedKey) {
        if (providedKey.isBlank()) {
            return false;
        }

        byte[] providedBytes = providedKey.getBytes(StandardCharsets.UTF_8);

        boolean valid = false;

        for (String expectedKey : securityProperties.inboundKeys().values()) {
            valid |= MessageDigest.isEqual(expectedKey.getBytes(StandardCharsets.UTF_8), providedBytes);
        }

        return valid;
    }

}