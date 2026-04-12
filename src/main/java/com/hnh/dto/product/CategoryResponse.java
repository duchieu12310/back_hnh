package com.hnh.dto.product;

import lombok.Data;

import java.time.Instant;
import java.util.List;

@Data
public class CategoryResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String slug;
    private Integer level;
    private ParentCategoryResponse parentCategory;
    private Integer status;
    private List<CategoryResponse> children;
}

