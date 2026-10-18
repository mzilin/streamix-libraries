package com.mariuszilinskas.streamix.web.config;

import com.mariuszilinskas.streamix.web.handler.CommonExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(CommonExceptionHandler.class)
public class CommonWebAutoConfiguration {
}