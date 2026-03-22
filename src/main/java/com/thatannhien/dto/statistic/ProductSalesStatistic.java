package com.thatannhien.dto.statistic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductSalesStatistic {
    private Long productId;
    private String productName;
    private String productCode;
    private Long totalQuantitySold;
    private Long totalRevenue;
}

