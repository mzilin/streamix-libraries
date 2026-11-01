package com.mariuszilinskas.streamix.security.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "security")
public record SecurityProperties(
        Map<String, String> inboundKeys,
        Map<String, String> outboundKeys
) {
    public SecurityProperties {
        inboundKeys = inboundKeys == null ? Map.of() : Map.copyOf(inboundKeys);
        outboundKeys = outboundKeys == null ? Map.of() : Map.copyOf(outboundKeys);
    }
}