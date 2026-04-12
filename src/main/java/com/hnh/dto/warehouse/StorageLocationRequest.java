package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
public class StorageLocationRequest {

    // Filter
    private List<Long> categoryIds;   // multi cấp 1,2,3
    private List<Long> productIds;    // multi product

    // Location
    private Long warehouseId;
    private String aisle;
    private String shelf;
    private String bin;

    // Update inventory
    private List<ItemDto> items;

    @Data
    public static class ItemDto {
        private Long productId;
        private Long variantId;
        private String sku;
        private Integer quantity;
    }
}
