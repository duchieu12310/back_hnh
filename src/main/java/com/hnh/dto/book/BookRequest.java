package com.hnh.dto.book;

import lombok.Data;

@Data
public class BookRequest {
    private String title;
    private String description;
    private Double price;
}
