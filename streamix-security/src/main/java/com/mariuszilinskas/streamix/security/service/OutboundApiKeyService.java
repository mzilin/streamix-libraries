package com.mariuszilinskas.streamix.security.service;

import org.springframework.http.HttpHeaders;

public interface OutboundApiKeyService {

    String getApiKey(String serviceName);

    void apply(String serviceName, HttpHeaders headers);

}
