package com.mariuszilinskas.streamix.observability.kafka;

import com.mariuszilinskas.streamix.observability.context.LogContext;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jspecify.annotations.NonNull;
import org.slf4j.MDC;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public final class KafkaProducerInterceptor implements ProducerInterceptor<Object, Object> {

    @Override
    @NonNull
    public ProducerRecord<Object, Object> onSend(@NonNull ProducerRecord<Object, Object> record) {
        String correlationId = MDC.get(LogContext.MDC_CORRELATION_ID);
        String userId = MDC.get(LogContext.MDC_USER_ID);

        if (correlationId != null) {
            record.headers().add(
                    LogContext.CORRELATION_ID_HEADER,
                    correlationId.getBytes(StandardCharsets.UTF_8)
            );
        }
        if (userId != null) {
            record.headers().add(LogContext.USER_ID_HEADER, userId.getBytes(StandardCharsets.UTF_8));
        }

        return record;
    }

    @Override
    public void close() {
    }

    @Override
    public void configure(Map<String, ?> configs) {
    }
}
