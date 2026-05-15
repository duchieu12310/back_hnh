package com.hnh.dto.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GlobalSearchResponse {
    private List<ClientListedProductResponse> products;
    private List<ClientCategoryResponse> categories;
    private List<ClientBrandResponse> brands;
}
