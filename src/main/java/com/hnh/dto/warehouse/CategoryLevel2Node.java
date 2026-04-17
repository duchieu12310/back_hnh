package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
@lombok.experimental.Accessors(chain = true)
public class CategoryLevel2Node {
    private Long id;
    private String name;
    private List<CategoryLevel3Node> children;
    private List<ProductStorageResponse> products;
}
