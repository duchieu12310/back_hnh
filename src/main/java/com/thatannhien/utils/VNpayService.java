package com.thatannhien.utils;

import com.thatannhien.config.VNPayConfig;
import com.thatannhien.dto.VNpayResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@AllArgsConstructor
public class VNpayService {
    private final VNPayConfig vnpayConfig;
    public String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "HmacSHA512");
            hmac.init(secretKeySpec);
            byte[] result = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            return String.format("%064x", new BigInteger(1, result));
        } catch (Exception e) {
            return "";
        }
    }

    public VNpayResult handleResult(Map<String, String[]> paramMap) {
        VNpayResult vnpayResult = new VNpayResult();
        
        // Safely get parameters with null checks
        String[] vnp_ResponseCodeArray = paramMap.get("vnp_ResponseCode");
        String[] vnp_TransactionStatusArray = paramMap.get("vnp_TransactionStatus");
        String[] vnp_TxnRefArray = paramMap.get("vnp_TxnRef");
        String[] vnp_SecureHashArray = paramMap.get("vnp_SecureHash");
        
        String vnp_ResponseCode = (vnp_ResponseCodeArray != null && vnp_ResponseCodeArray.length > 0) 
            ? vnp_ResponseCodeArray[0] : null;
        String vnp_TransactionStatus = (vnp_TransactionStatusArray != null && vnp_TransactionStatusArray.length > 0) 
            ? vnp_TransactionStatusArray[0] : null;
        String vnp_TxnRef = (vnp_TxnRefArray != null && vnp_TxnRefArray.length > 0) 
            ? vnp_TxnRefArray[0] : null;
        String vnp_SecureHash = (vnp_SecureHashArray != null && vnp_SecureHashArray.length > 0) 
            ? vnp_SecureHashArray[0] : null;
        
        System.out.println("vnp_ResponseCode = " + vnp_ResponseCode);
        System.out.println("vnp_TransactionStatus = " + vnp_TransactionStatus);
        System.out.println("vnp_TxnRef = " + vnp_TxnRef);
        System.out.println("vnp_SecureHash = " + vnp_SecureHash);
        
        // Set order ID if available
        if (vnp_TxnRef == null || vnp_TxnRef.isEmpty()) {
            System.out.println("Order ID is missing");
            vnpayResult.setOrderId("");
            vnpayResult.setSuccess(false);
            return vnpayResult;
        }
        vnpayResult.setOrderId(vnp_TxnRef);
        
        try {
            // Verify signature first - CRITICAL for security
            if (vnp_SecureHash == null || vnp_SecureHash.isEmpty()) {
                System.out.println("VNPay SecureHash is missing - rejecting transaction");
                vnpayResult.setSuccess(false);
                return vnpayResult;
            }
            
            // Build hash data from all parameters except vnp_SecureHash
            Map<String, String> vnpParams = new HashMap<>();
            for (Map.Entry<String, String[]> entry : paramMap.entrySet()) {
                String key = entry.getKey();
                if (!key.equals("vnp_SecureHash") && entry.getValue() != null && entry.getValue().length > 0) {
                    vnpParams.put(key, entry.getValue()[0]);
                }
            }
            
            // Sort parameters alphabetically
            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);
            
            // Build hash data string
            StringBuilder hashData = new StringBuilder();
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName);
                    hashData.append('=');
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                    if (i < fieldNames.size() - 1) {
                        hashData.append('&');
                    }
                }
            }
            
            // Calculate hash
            String calculatedHash = hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            
            // Verify signature
            if (!calculatedHash.equals(vnp_SecureHash)) {
                System.out.println("VNPay signature verification FAILED!");
                System.out.println("Expected: " + calculatedHash);
                System.out.println("Received: " + vnp_SecureHash);
                System.out.println("Hash data: " + hashData.toString());
                vnpayResult.setSuccess(false);
                return vnpayResult;
            }
            
            System.out.println("VNPay signature verification SUCCESS");
            
            // Check transaction status
            if (vnp_ResponseCode != null && vnp_TransactionStatus != null 
                && vnp_ResponseCode.equals("00") && vnp_TransactionStatus.equals("00")) {
                System.out.println("Thanh toan thanh cong");
                vnpayResult.setSuccess(true);
            } else {
                System.out.println("Thanh toan that bai - ResponseCode: " + vnp_ResponseCode + ", TransactionStatus: " + vnp_TransactionStatus);
                vnpayResult.setSuccess(false);
            }
        } catch (Exception e) {
            System.out.println("Error processing VNPay result: " + e.getMessage());
            e.printStackTrace();
            vnpayResult.setSuccess(false);
        }
        
        return vnpayResult;
    }

    public String getPayUrl(String orderId, long totalPrice, String clientIp) {
        try {
            // Validate amount: VNPay requires amount between 5,000 and 999,999,999 VND
            // Amount is in VND, need to convert to cents (multiply by 100)
            long amountInCents = totalPrice * 100;
            long minAmount = 500000; // 5,000 VND in cents
            long maxAmount = 99999999900L; // 999,999,999 VND in cents
            
            if (amountInCents < minAmount || amountInCents > maxAmount) {
                System.out.println("Invalid amount: " + totalPrice + " VND (must be between 5,000 and 999,999,999 VND)");
                throw new IllegalArgumentException("Số tiền không hợp lệ. Số tiền hợp lệ từ 5,000 đến dưới 1 tỷ đồng");
            }
            
            // Set timezone
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"));

            LocalDateTime datetime = LocalDateTime.now();
            DateTimeFormatter createDateFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
            String createDate = datetime.format(createDateFormatter);
            LocalDateTime expireDate = datetime.plusMinutes(15);
            String expireDateStr = expireDate.format(createDateFormatter);
            
            // Use client IP if provided, otherwise use default
            String ipAddr = (clientIp != null && !clientIp.isEmpty()) ? clientIp : "127.0.0.1";

            Map<String, String> vnpParams = new HashMap<>();
            vnpParams.put("vnp_Version", "2.1.0");
            vnpParams.put("vnp_Command", "pay");
            vnpParams.put("vnp_TmnCode", vnpayConfig.getVnp_TmnCode());
            vnpParams.put("vnp_Locale", "vn");
            vnpParams.put("vnp_CurrCode", "VND");
            vnpParams.put("vnp_TxnRef", orderId);
            vnpParams.put("vnp_OrderInfo", "Thanh toan cho ma GD:" + orderId);
            vnpParams.put("vnp_OrderType", "billpayment");
            vnpParams.put("vnp_Amount", String.valueOf(amountInCents)); // Amount in cents
            vnpParams.put("vnp_ReturnUrl", vnpayConfig.getVnp_ReturnUrl());
            vnpParams.put("vnp_IpAddr", ipAddr);
            vnpParams.put("vnp_CreateDate", createDate);
            vnpParams.put("vnp_ExpireDate", expireDateStr);
            // Thêm BankCode để tránh lỗi "Ngân hàng không được hỗ trợ" khi test
            // NCB = Ngân hàng Quốc Dân (thường dùng để test VNPay sandbox)
            // Có thể để null nếu muốn user tự chọn ngân hàng trên trang VNPay
            vnpParams.put("vnp_BankCode", "NCB");

            // Sort parameters alphabetically
            List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
            Collections.sort(fieldNames);

            // Build hash data and query string
            StringBuilder hashData = new StringBuilder();
            StringBuilder query = new StringBuilder();
            
            // First, collect all non-empty fields
            List<String> validFields = new ArrayList<>();
            for (String fieldName : fieldNames) {
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    validFields.add(fieldName);
                }
            }
            
            // Build hash data and query string
            for (int i = 0; i < validFields.size(); i++) {
                String fieldName = validFields.get(i);
                String fieldValue = vnpParams.get(fieldName);
                
                // Build hash data (for signature) - use UTF-8 encoding
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                
                // Build query string (for URL) - use UTF-8 encoding
                query.append(URLEncoder.encode(fieldName, StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.UTF_8));
                
                if (i < validFields.size() - 1) {
                    hashData.append('&');
                    query.append('&');
                }
            }

            // Create secure hash
            String secureHash = hmacSHA512(vnpayConfig.getSecretKey(), hashData.toString());
            query.append("&vnp_SecureHash=").append(URLEncoder.encode(secureHash, StandardCharsets.UTF_8));

            // Build payment URL
            String paymentUrl = vnpayConfig.getVnp_PayUrl() + "?" + query;
            System.out.println("VNPay Payment URL created for order: " + orderId + ", amount: " + totalPrice + " VND");
            
            return paymentUrl;
        } catch (Exception e) {
            System.out.println("Error creating VNPay payment URL: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // Overload method for backward compatibility
    public String getPayUrl(String orderId, int totalPrice) {
        return getPayUrl(orderId, (long) totalPrice, null);
    }

    public static void main(String[] args) {
        var cfg=new VNPayConfig();

        cfg.setVnp_PayUrl("https://sandbox.vnpayment.vn/paymentv2/vpcpay.html");
        cfg.setVnp_ReturnUrl("http://localhost:8085/vnpay/return");
        cfg.setVnp_TmnCode("4FRV9TJJ");
        cfg.setSecretKey("HV58GJB2A8CF07K6DI5J0VN036NALUXF");
        cfg.setVnp_ApiUrl("https://sandbox.vnpayment.vn/merchant_webapi/api/transaction");
        BigDecimal a = new BigDecimal("35750000.00000");

        VNpayService vNpayService = new VNpayService(cfg);
        System.out.println(vNpayService.getPayUrl("123", a.intValue()));
    }

}
