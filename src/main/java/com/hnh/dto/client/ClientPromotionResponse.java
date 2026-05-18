package com.hnh.dto.client;

import lombok.Data;

@Data
public class ClientPromotionResponse {
    private Long promotionId;
    private Integer promotionPercent;
    private String promotionName;
    private java.time.Instant startDate;
    private java.time.Instant endDate;
}

