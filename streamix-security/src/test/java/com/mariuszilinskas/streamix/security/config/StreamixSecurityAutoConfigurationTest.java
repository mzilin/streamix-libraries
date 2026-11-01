package com.mariuszilinskas.streamix.security.config;

import com.mariuszilinskas.streamix.security.filter.ApiKeyFilter;
import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import com.mariuszilinskas.streamix.security.service.OutboundApiKeyServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class StreamixSecurityAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(StreamixSecurityAutoConfiguration.class));

    @Test
    void registersApiKeyFilterWhenInboundKeysAreConfigured() {
        contextRunner
                .withPropertyValues("security.inbound-keys.gateway=dummy-api-key")
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

    @Test
    void registersApiKeyFilterWhenNoInboundKeysAreConfigured() {
        contextRunner
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

    @Test
    void doesNotRegisterApiKeyFilterWhenBeanAlreadyExists() {
        contextRunner
                .withPropertyValues("security.inbound-keys.gateway=dummy-api-key")
                .withBean(ApiKeyFilter.class, () -> new ApiKeyFilter(new SecurityProperties(null, null)))
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

    @Test
    void registersOutboundApiKeyServiceWhenOutboundKeysAreConfigured() {
        contextRunner
                .withPropertyValues("security.outbound-keys.service-b=dummy-outbound-key")
                .run(context -> assertThat(context).hasSingleBean(OutboundApiKeyServiceImpl.class));
    }

    @Test
    void registersOutboundApiKeyServiceWhenNoOutboundKeysAreConfigured() {
        contextRunner
                .run(context -> assertThat(context).hasSingleBean(OutboundApiKeyServiceImpl.class));
    }

    @Test
    void doesNotRegisterOutboundApiKeyServiceWhenBeanAlreadyExists() {
        contextRunner
                .withPropertyValues("security.outbound-keys.service-b=dummy-outbound-key")
                .withBean(OutboundApiKeyServiceImpl.class, () -> new OutboundApiKeyServiceImpl(new SecurityProperties(null, null)))
                .run(context -> assertThat(context).hasSingleBean(OutboundApiKeyServiceImpl.class));
    }

}