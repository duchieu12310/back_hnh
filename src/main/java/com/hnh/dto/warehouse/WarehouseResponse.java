package com.hnh.dto.warehouse;

import com.hnh.dto.address.AddressResponse;
import com.hnh.dto.product.CategoryResponse;
import com.hnh.dto.product.ProductResponse;
import lombok.Data;
import java.time.Instant;
import java.util.Set;

@Data
public class WarehouseResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String code;
    private Integer status;
    private AddressResponse address;
    private Set<CategoryResponse> categories;
    private Set<ProductResponse> products;
}
