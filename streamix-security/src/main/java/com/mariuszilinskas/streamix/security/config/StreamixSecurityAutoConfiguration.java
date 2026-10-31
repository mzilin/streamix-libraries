package com.mariuszilinskas.streamix.security.config;

import com.mariuszilinskas.streamix.security.filter.ApiKeyFilter;
import com.mariuszilinskas.streamix.security.properties.SecurityProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@EnableConfigurationProperties(SecurityProperties.class)
public class StreamixSecurityAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ApiKeyFilter.class)
    public ApiKeyFilter apiKeyFilter(SecurityProperties securityProperties) {
        return new ApiKeyFilter(securityProperties);
    }

}