package com.hnh.dto.client;

import com.hnh.entity.cashbook.PaymentMethodType;
import lombok.Data;

@Data
public class ClientSimpleOrderRequest {
    private PaymentMethodType paymentMethodType;
}

