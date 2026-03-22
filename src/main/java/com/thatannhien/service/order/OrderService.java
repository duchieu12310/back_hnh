package com.thatannhien.service.order;

import com.thatannhien.dto.client.ClientConfirmedOrderResponse;
import com.thatannhien.dto.client.ClientSimpleOrderRequest;

public interface OrderService {

    void cancelOrder(String code);

    ClientConfirmedOrderResponse createClientOrder(ClientSimpleOrderRequest request);

    void captureTransactionPaypal(String paypalOrderId, String payerId);
    void captureTransactionVNPay(String vnpayOrderId, boolean isSuccess);
}
