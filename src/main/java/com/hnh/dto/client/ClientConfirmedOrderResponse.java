package com.hnh.dto.client;

import com.hnh.entity.cashbook.PaymentMethodType;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ClientConfirmedOrderResponse {
    private String orderCode;
    private PaymentMethodType orderPaymentMethodType;
    @Nullable
    private String orderPaypalCheckoutLink;
}

