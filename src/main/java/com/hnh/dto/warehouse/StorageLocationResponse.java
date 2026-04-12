package com.hnh.dto.warehouse;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class StorageLocationResponse {

    private Long id;
    private Instant createdAt;
    private Instant updatedAt;

    private Long warehouseId;
    private String warehouseName;

    private String aisle;
    private String shelf;
    private String bin;

    private List<ItemDto> items;

    @Data
    public static class ItemDto {
        private Long productId;
        private String productName;

        private Long variantId;
        private String sku;

        private Integer quantity;
    }
}
