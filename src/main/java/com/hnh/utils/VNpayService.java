package com.hnh.utils;

import com.hnh.config.VNPayConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class VNpayService {
    private final VNPayConfig vnpayConfig;

    public String hmacSHA512(String key, String data) {
        try {
            if (key == null || data == null) {
                log.error("Key or data for HMAC SHA512 is null");
                return "";
            }
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] result = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : result) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString(); // VNPay requires Lowercase Hex
        } catch (Exception e) {
            log.error("Error calculating HMAC SHA512", e);
            return "";
        }
    }

    public com.hnh.dto.VNpayResult handleResult(Map<String, String[]> paramMap) {
        com.hnh.dto.VNpayResult vnpayResult = new com.hnh.dto.VNpayResult();
        
        String vnp_SecureHash = (paramMap.get("vnp_SecureHash") != null && paramMap.get("vnp_SecureHash").length > 0) 
            ? paramMap.get("vnp_SecureHash")[0] : null;
        String vnp_TxnRef = (paramMap.get("vnp_TxnRef") != null && paramMap.get("vnp_TxnRef").length > 0) 
            ? paramMap.get("vnp_TxnRef")[0] : null;
        String vnp_ResponseCode = (paramMap.get("vnp_ResponseCode") != null && paramMap.get("vnp_ResponseCode").length > 0) 
            ? paramMap.get("vnp_ResponseCode")[0] : null;
        String vnp_TransactionStatus = (paramMap.get("vnp_TransactionStatus") != null && paramMap.get("vnp_TransactionStatus").length > 0) 
            ? paramMap.get("vnp_TransactionStatus")[0] : null;

        log.info("Processing VNPay result for TxnRef: {}, ResponseCode: {}", vnp_TxnRef, vnp_ResponseCode);
        
        vnpayResult.setOrderId(vnp_TxnRef);
        
        try {
            if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
                log.error("VNPay SecureHash is missing");
                vnpayResult.setSuccess(false);
                return vnpayResult;
            }

            // Build hash data from all parameters except security fields
            List<String> fieldNames = new ArrayList<>(paramMap.keySet());
            Collections.sort(fieldNames);

            // Refined string building
            StringBuilder hashData = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = (paramMap.get(fieldName) != null && paramMap.get(fieldName).length > 0) 
                    ? paramMap.get(fieldName)[0] : null;

                if (fieldValue != null && !fieldValue.isEmpty() 
                    && !fieldName.equals("vnp_SecureHash") 
                    && !fieldName.equals("vnp_SecureHashType")) {

                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    
                    if (itr.hasNext()) {
                        hashData.append('&');
                    }
                }
            }
            
            // Clean up trailing '&' if the last valid entry was ignored
            if (hashData.length() > 0 && hashData.charAt(hashData.length() - 1) == '&') {
                hashData.setLength(hashData.length() - 1);
            }

            String calculatedHash = hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            
            if (!calculatedHash.equalsIgnoreCase(vnp_SecureHash)) {
                log.error("VNPay signature verification FAILED!");
                log.debug("Calculated: {}, Received: {}", calculatedHash, vnp_SecureHash);
                vnpayResult.setSuccess(false);
                return vnpayResult;
            }
            
            if ("00".equals(vnp_ResponseCode) && "00".equals(vnp_TransactionStatus)) {
                vnpayResult.setSuccess(true);
            } else {
                vnpayResult.setSuccess(false);
            }
        } catch (Exception e) {
            log.error("Error verifying VNPay signature", e);
            vnpayResult.setSuccess(false);
        }
        
        return vnpayResult;
    }

    public String getPayUrl(String orderId, long totalPrice, String clientIp) {
        try {
            long amountInCents = totalPrice * 100;
            
            LocalDateTime datetime = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String createDate = datetime.format(formatter);
            String expireDate = datetime.plusMinutes(15).format(formatter);

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
            vnpParams.put("vnp_Amount", String.valueOf(amountInCents));
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD:" + orderId);
            vnpParams.put("vnp_OrderType", "other");
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
            vnpParams.put("vnp_IpAddr", (clientIp != null && !clientIp.isEmpty()) ? clientIp : "127.0.0.1");
            vnpParams.put("vnp_CreateDate", createDate);
            vnpParams.put("vnp_ExpireDate", expireDate);
            vnpParams.put("vnp_BankCode", "NCB"); // Recommended for testing

            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            Iterator<String> itr = fieldNames.iterator();
            while (itr.hasNext()) {
                String fieldName = itr.next();
                String fieldValue = vnpParams.get(fieldName);
                if ((fieldValue != null) && (fieldValue.length() > 0)) {
                    //Build hash data
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    //Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    if (itr.hasNext()) {
                        query.append('&');
                        hashData.append('&');
                    }
                }
            }
            String queryUrl = query.toString();
            String vnp_SecureHash = hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
            String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + queryUrl;
            
            log.info("Payment URL: {}", paymentUrl);
            return paymentUrl;
        } catch (Exception e) {
            log.error("Error creating payment URL", e);
            throw new RuntimeException(e);
        }
    }

    public String getPayUrl(String orderId, int totalPrice) {
        return getPayUrl(orderId, (long) totalPrice, null);
    }
}
