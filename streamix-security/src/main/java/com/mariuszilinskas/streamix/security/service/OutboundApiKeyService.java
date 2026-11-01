package com.mariuszilinskas.streamix.security.service;

import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import org.springframework.http.HttpHeaders;

public class OutboundApiKeyService {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final SecurityProperties securityProperties;

    public OutboundApiKeyService(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    public void apply(String serviceName, HttpHeaders headers) {
        String apiKey = securityProperties.outboundKeys().get(serviceName);

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException("No outbound API key configured for service: " + serviceName);
        }

        headers.set(API_KEY_HEADER, apiKey);
    }

}
