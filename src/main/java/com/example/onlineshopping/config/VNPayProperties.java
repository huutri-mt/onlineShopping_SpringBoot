package com.example.onlineshopping.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "vnpay")
public class VNPayProperties {
    private String vnpTmnCode;
    private String vnpHashSecret;
    private String vnpUrl;
    private String returnUrl;
}
