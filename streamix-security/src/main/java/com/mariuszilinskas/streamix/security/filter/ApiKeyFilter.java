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
import java.util.Map;

@NullMarked
@RequiredArgsConstructor
public class ApiKeyFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final SecurityProperties securityProperties;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String apiKey = request.getHeader(AUTHORIZATION_HEADER);
        Map<String, String> clientKeys = securityProperties.clientKeys();

        if (apiKey == null || clientKeys == null || !clientKeys.containsValue(apiKey)) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        filterChain.doFilter(request, response);
    }

}