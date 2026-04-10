package com.hnh.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@Data
public class VNPayConfig {
    @Value("${vnp.url}")
    private   String vnp_PayUrl;
    @Value("${vnp.returnUrl}")
    private   String vnp_ReturnUrl;
    @Value("${vnp.tmnCode}")
    private   String vnp_TmnCode;
    @Value("${vnp.secretKey}")
    private   String secretKey;
    @Value("${vnp.apiUrl}")
    private   String vnp_ApiUrl ;
}

