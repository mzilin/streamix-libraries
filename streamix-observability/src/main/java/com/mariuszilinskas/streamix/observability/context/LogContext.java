package com.mariuszilinskas.streamix.observability.context;

public final class LogContext {

    private LogContext() {}

    public static final String MDC_CORRELATION_ID = "correlation_id";
    public static final String MDC_USER_ID = "user_id";
    public static final String MDC_SERVICE = "service";
    public static final String MDC_ENVIRONMENT = "environment";

    public static final String CORRELATION_ID_HEADER = "X-Correlation-Id";
    public static final String USER_ID_HEADER = "X-User-Id";

}
