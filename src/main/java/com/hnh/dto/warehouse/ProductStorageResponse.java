package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
@lombok.experimental.Accessors(chain = true)
public class ProductStorageResponse {
    private Long productId;
    private String productName;
    private String productCode;

    // Thông tin vị trí kho (Lấy từ StorageLocation)
    // Có thể để trống nếu sản phẩm chưa được xếp chỗ
    private Long storageLocationId;
    private String aisle;
    private String shelf;
    private String bin;

    // Danh sách các biến thể thuộc sản phẩm này tại vị trí này
    private List<VariantInventoryDto> variants;
}
