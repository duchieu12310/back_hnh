package com.hnh.service.order;

import com.hnh.dto.client.ClientConfirmedOrderResponse;
import com.hnh.dto.client.ClientSimpleOrderRequest;

public interface OrderService {

    void cancelOrder(String code);

    ClientConfirmedOrderResponse createClientOrder(ClientSimpleOrderRequest request);

    void captureTransactionPaypal(String paypalOrderId, String payerId);
    void captureTransactionVNPay(String vnpayOrderId, boolean isSuccess);
}

