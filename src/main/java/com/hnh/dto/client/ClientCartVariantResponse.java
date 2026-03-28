package com.hnh.dto.client;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.lang.Nullable;

@Data
public class ClientCartVariantResponse {
    private ClientVariantResponse cartItemVariant;
    private Integer cartItemQuantity;

    @Data
    public static class ClientVariantResponse {
        private Long variantId;
        private ClientProductResponse variantProduct;
        private Double variantPrice;
        private Double variantCost;
        @Nullable
        private String variantSku;
        @Nullable
        private JsonNode variantProperties;
        @Nullable
        private JsonNode variantImages;
        private Integer variantStatus;
        private Integer quantity;
        private String inventoryStatus;

        @Data
        public static class ClientProductResponse {
            private Long productId;
            private String productName;
            private String productSlug;
            @Nullable
            private String productThumbnail;
            @Nullable
            private ClientPromotionResponse productPromotion;
        }
    }
}

