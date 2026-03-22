package com.thatannhien.config;


import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;

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
