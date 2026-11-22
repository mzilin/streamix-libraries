package com.mariuszilinskas.streamix.web.exception;

import lombok.Getter;

@Getter
public class DownstreamServiceException extends RuntimeException {

    private final String serviceName;

    public DownstreamServiceException(String serviceName, Throwable cause) {
        super("An unexpected error occurred.", cause);
        this.serviceName = serviceName;
    }

}
