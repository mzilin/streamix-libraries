package com.mariuszilinskas.streamix.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        Map<String, String> clientKeys
) {}