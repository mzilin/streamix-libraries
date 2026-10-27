package com.mariuszilinskas.streamix.observability.config;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@AutoConfiguration
@ConditionalOnClass({OpenTelemetryAppender.class, LoggerContext.class, OpenTelemetry.class})
@ConditionalOnBean(OpenTelemetry.class)
@AutoConfigureAfter(name = "org.springframework.boot.opentelemetry.autoconfigure.OpenTelemetrySdkAutoConfiguration")
public class StreamixLoggingAutoConfiguration {

    @Bean(destroyMethod = "stop")
    @ConditionalOnMissingBean(OpenTelemetryAppender.class)
    @Profile("!ci")
    public OpenTelemetryAppender openTelemetryLogbackAppender(OpenTelemetry openTelemetry) {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();

        OpenTelemetryAppender appender = new OpenTelemetryAppender();
        appender.setContext(context);
        appender.setOpenTelemetry(openTelemetry);
        appender.start();

        Logger rootLogger = context.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);

        return appender;
    }
}
