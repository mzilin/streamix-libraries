package com.mariuszilinskas.streamix.security.service;

import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OutboundApiKeyServiceTest {

    private static final String SERVICE_NAME = "service-b";
    private static final String OUTBOUND_KEY = "outbound-secret";

    private final SecurityProperties properties = new SecurityProperties(null, Map.of(SERVICE_NAME, OUTBOUND_KEY));
    private final OutboundApiKeyServiceImpl service = new OutboundApiKeyServiceImpl(properties);

    @Test
    void applySetsApiKeyHeaderForConfiguredService() {
        HttpHeaders headers = new HttpHeaders();

        service.apply(SERVICE_NAME, headers);

        assertThat(headers.getFirst("X-API-Key")).isEqualTo(OUTBOUND_KEY);
    }

    @Test
    void applyThrowsWhenServiceNotConfigured() {
        HttpHeaders headers = new HttpHeaders();

        assertThatThrownBy(() -> service.apply("unknown-service", headers))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unknown-service");
    }

    @Test
    void applyThrowsWhenKeyIsBlank() {
        SecurityProperties blankKeyProperties = new SecurityProperties(null, Map.of(SERVICE_NAME, "  "));
        OutboundApiKeyServiceImpl blankKeyService = new OutboundApiKeyServiceImpl(blankKeyProperties);
        HttpHeaders headers = new HttpHeaders();

        assertThatThrownBy(() -> blankKeyService.apply(SERVICE_NAME, headers))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(SERVICE_NAME);
    }

    @Test
    void getApiKeyReturnsKeyForConfiguredService() {
        assertThat(service.getApiKey(SERVICE_NAME)).isEqualTo(OUTBOUND_KEY);
    }

    @Test
    void getApiKeyThrowsWhenServiceNotConfigured() {
        assertThatThrownBy(() -> service.getApiKey("unknown-service"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("unknown-service");
    }

    @Test
    void getApiKeyThrowsWhenKeyIsBlank() {
        SecurityProperties blankKeyProperties = new SecurityProperties(null, Map.of(SERVICE_NAME, "  "));
        OutboundApiKeyServiceImpl blankKeyService = new OutboundApiKeyServiceImpl(blankKeyProperties);

        assertThatThrownBy(() -> blankKeyService.getApiKey(SERVICE_NAME))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining(SERVICE_NAME);
    }

}