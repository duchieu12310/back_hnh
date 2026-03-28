package com.hnh.projection.inventory;

import com.hnh.entity.inventory.DocketVariant;
import com.hnh.entity.product.Product;
import lombok.Data;

import java.util.List;

@Data
public class ProductInventory {
    private Product product;
    private List<DocketVariant> transactions;
    private Integer inventory;
    private Integer waitingForDelivery;
    private Integer canBeSold;
    private Integer areComing;
}

