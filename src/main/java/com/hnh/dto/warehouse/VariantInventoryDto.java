package com.hnh.dto.warehouse;

import lombok.Data;

@Data
@lombok.experimental.Accessors(chain = true)
public class VariantInventoryDto {
    private Long variantId;
    private String sku;
    private String properties; // VD: {"color": "Red", "size": "XL"}
    
    // Số lượng hiện có tại vị trí (aisle/shelf/bin) này
    private Integer quantityInLocation; 
    
    // Tổng số lượng của biến thể này trên toàn hệ thống (để đối soát)
    private Integer totalVariantQuantity; 
}
