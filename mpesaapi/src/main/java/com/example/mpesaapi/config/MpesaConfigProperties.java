package com.example.mpesaapi.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
@ConfigurationProperties(prefix = "mpesa")
public class MpesaConfigProperties {
    private String appKey;
    private String appSecret;
    private String businessShortcode;
    private String passkey;
    private String callbackUrl;
    private String timeoutUrl;
    private String env;

  
}