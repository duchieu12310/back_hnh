package com.hnh.dto.product;

import lombok.Data;

@Data
public class CategoryRequest {
    private String name;
    private String slug;
    private Integer level;
    private Long parentCategoryId;
    private Integer status;
}

