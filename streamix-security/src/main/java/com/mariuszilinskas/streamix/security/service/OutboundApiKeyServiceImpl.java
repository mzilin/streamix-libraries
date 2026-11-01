package com.mariuszilinskas.streamix.security.service;

import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import org.springframework.http.HttpHeaders;

public class OutboundApiKeyServiceImpl implements OutboundApiKeyService {

    private static final String API_KEY_HEADER = "X-API-Key";

    private final SecurityProperties securityProperties;

    public OutboundApiKeyServiceImpl(SecurityProperties securityProperties) {
        this.securityProperties = securityProperties;
    }

    @Override
    public String getApiKey(String serviceName) {
        return resolveApiKey(serviceName);
    }

    @Override
    public void apply(String serviceName, HttpHeaders headers) {
        headers.set(API_KEY_HEADER, resolveApiKey(serviceName));
    }

    private String resolveApiKey(String serviceName) {
        String apiKey = securityProperties.outboundKeys().get(serviceName);

        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalStateException(
                    "No outbound API key configured for service: " + serviceName
            );
        }

        return apiKey;
    }
}
