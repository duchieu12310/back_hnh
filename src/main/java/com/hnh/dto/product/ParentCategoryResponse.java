package com.hnh.dto.product;

import lombok.Data;

import java.time.Instant;

@Data
public class ParentCategoryResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private String name;
    private String slug;
    private Integer level;
    private Integer status;
}

