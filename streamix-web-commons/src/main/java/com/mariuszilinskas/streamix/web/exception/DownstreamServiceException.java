package com.mariuszilinskas.streamix.web.exception;

public class DownstreamServiceException extends RuntimeException {

    public DownstreamServiceException(Throwable cause) {
        super("An unexpected error occurred.", cause);
    }

}
