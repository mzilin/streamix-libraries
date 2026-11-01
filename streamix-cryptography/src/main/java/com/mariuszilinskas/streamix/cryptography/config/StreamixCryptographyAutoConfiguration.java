package com.mariuszilinskas.streamix.cryptography.config;

import com.mariuszilinskas.streamix.cryptography.kms.KmsFieldEncryptionServiceImpl;
import com.mariuszilinskas.streamix.cryptography.local.LocalKeyFieldEncryptionServiceImpl;
import com.mariuszilinskas.streamix.cryptography.properties.CryptographyProperties;
import com.mariuszilinskas.streamix.cryptography.service.FieldEncryptionService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsClient;

@AutoConfiguration
@EnableConfigurationProperties(CryptographyProperties.class)
public class StreamixCryptographyAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(FieldEncryptionService.class)
    @ConditionalOnProperty(prefix = "cryptography", name = "provider", havingValue = "local")
    public FieldEncryptionService localFieldEncryptionService(CryptographyProperties properties) {
        return new LocalKeyFieldEncryptionServiceImpl(properties.encryptionKey());
    }

    @Bean
    @ConditionalOnMissingBean(KmsClient.class)
    @ConditionalOnProperty(prefix = "cryptography", name = "provider", havingValue = "kms")
    public KmsClient kmsClient(CryptographyProperties properties) {
        return KmsClient.builder()
                .region(Region.of(properties.awsRegion()))
                .build();
    }

    @Bean
    @ConditionalOnMissingBean(FieldEncryptionService.class)
    @ConditionalOnProperty(prefix = "cryptography", name = "provider", havingValue = "kms")
    public FieldEncryptionService kmsFieldEncryptionService(
            KmsClient kmsClient,
            CryptographyProperties properties
    ) {
        return new KmsFieldEncryptionServiceImpl(kmsClient, properties.kmsKeyId());
    }
}