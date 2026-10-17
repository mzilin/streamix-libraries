package com.mariuszilinskas.streamix.observability.config;

import com.mariuszilinskas.streamix.observability.rabbitmq.RabbitMqConsumerInterceptor;
import com.mariuszilinskas.streamix.observability.rabbitmq.RabbitMqProducerPostProcessor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class StreamixRabbitObservabilityAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withConfiguration(AutoConfigurations.of(StreamixRabbitObservabilityAutoConfiguration.class));

    @Test
    void registersBothBeans() {
        contextRunner.run(context -> {
            assertThat(context).hasSingleBean(RabbitMqConsumerInterceptor.class);
            assertThat(context).hasSingleBean(RabbitMqProducerPostProcessor.class);
        });
    }

    @Test
    void doesNotRegisterConsumerInterceptorWhenUserProvidesOne() {
        contextRunner
                .withBean(RabbitMqConsumerInterceptor.class, () -> new RabbitMqConsumerInterceptor("svc", "test"))
                .run(context -> assertThat(context).hasSingleBean(RabbitMqConsumerInterceptor.class));
    }

    @Test
    void doesNotRegisterProducerPostProcessorWhenUserProvidesOne() {
        contextRunner
                .withBean(RabbitMqProducerPostProcessor.class, RabbitMqProducerPostProcessor::new)
                .run(context -> assertThat(context).hasSingleBean(RabbitMqProducerPostProcessor.class));
    }
}
