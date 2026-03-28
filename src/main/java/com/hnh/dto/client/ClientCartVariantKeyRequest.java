package com.hnh.dto.client;

import lombok.Data;

@Data
public class ClientCartVariantKeyRequest {
    private Long cartId;
    private Long variantId;
}

