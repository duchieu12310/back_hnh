package com.hnh.dto.warehouse;

import lombok.Data;

@Data
public class AutoSaveInventoryRequest {
    private Long warehouseId;
    private Long variantId;
    private Long storageLocationId;
    private Integer newQuantity;
}
