package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
public class WarehouseRequest {
    private String name;
    private String code;
    private Integer status;
    private AddressDto address;
    
    // Danh sách các danh mục, mỗi danh mục chứa danh sách sản phẩm riêng
    private List<CategorySelectionDto> categories;

    @Data
    public static class CategorySelectionDto {
        private Long id;
        private String name;
        private List<Long> productIds; // Danh sách sản phẩm thuộc danh mục này
    }

    @Data
    public static class AddressDto {
        private String line;
        private IdDto province;
        private IdDto district;
        private IdDto ward;
    }

    @Data
    public static class IdDto {
        private Long id;
        private String name;
    }
}
