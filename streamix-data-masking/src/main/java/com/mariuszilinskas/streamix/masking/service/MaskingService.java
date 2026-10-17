package com.mariuszilinskas.streamix.masking.service;

public interface MaskingService {
    String maskFull(String value);
    String maskPartial(String value);
    String maskEmail(String value);
}
