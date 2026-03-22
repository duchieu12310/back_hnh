package com.thatannhien.dto.client;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class ClientNewsletterSubscriptionResponse {
    private Long subscriptionId;
    private String email;
    private Integer status;
    private Instant createdAt;
}

