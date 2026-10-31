package com.mariuszilinskas.streamix.security.config;

import com.mariuszilinskas.streamix.security.filter.ApiKeyFilter;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class StreamixSecurityAutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(StreamixSecurityAutoConfiguration.class));

    @Test
    void registersApiKeyFilterWhenClientKeysAreConfigured() {
        contextRunner
                .withPropertyValues("security.client-keys.gateway=dummy-api-key")
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

    @Test
    void registersApiKeyFilterWhenNoClientKeysAreConfigured() {
        contextRunner
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

    @Test
    void doesNotRegisterApiKeyFilterWhenBeanAlreadyExists() {
        contextRunner
                .withPropertyValues("security.client-keys.gateway=dummy-api-key")
                .withBean(ApiKeyFilter.class, () -> new ApiKeyFilter(new com.mariuszilinskas.streamix.security.properties.SecurityProperties(null)))
                .run(context -> assertThat(context).hasSingleBean(ApiKeyFilter.class));
    }

}