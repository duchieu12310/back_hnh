package com.hnh.dto.warehouse;

import lombok.Data;
import java.util.List;

@Data
@lombok.experimental.Accessors(chain = true)
public class CategoryLevel3Node {
    private Long id;
    private String name;
    private List<ProductStorageResponse> products;
}
