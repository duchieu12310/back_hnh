package com.hnh.dto.waybill;

import com.hnh.dto.order.OrderResponse;
import com.hnh.entity.waybill.RequiredNote;
import lombok.Data;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
public class WaybillResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String code;
    private OrderResponse order;
    private Instant shippingDate;
    private Instant expectedDeliveryTime;
    private Integer status;
    private Integer codAmount;
    private Integer shippingFee;
    private Integer weight;
    private Integer length;
    private Integer width;
    private Integer height;
    @Nullable
    private String note;
    private Integer ghnPaymentTypeId;
    private RequiredNote ghnRequiredNote;
}

