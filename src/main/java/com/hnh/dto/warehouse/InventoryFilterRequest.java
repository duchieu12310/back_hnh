package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
public class InventoryFilterRequest {
    // Thông tin vị trí kho (Location) - Dùng để xác định đích đến
    private Long warehouseId;
    private String aisle;
    private String shelf;
    private String bin;

    // Bộ lọc đa cấp - Mặc định rỗng/null nghĩa là "ALL"
    private List<Long> categoryL1Ids; // Danh mục cha (level 1)
    private List<Long> categoryL2Ids; // Danh mục trung gian (level 2)
    private List<Long> categoryL3Ids; // Danh mục lá (level 3)

    
    // Lọc theo sản phẩm cụ thể (nếu user chọn đích danh)
    private List<Long> productIds;
}
