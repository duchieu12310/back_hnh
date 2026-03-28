package com.hnh.mapper.product;

import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.entity.product.Product;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.general.ImageMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, ImageMapper.class, BrandMapper.class, SupplierMapper.class, UnitMapper.class,
                GuaranteeMapper.class})
public interface ProductMapper extends GenericMapper<Product, ProductRequest, ProductResponse> {

    @AfterMapping
    default void setInventoryStatus(@MappingTarget ProductResponse response) {
        if (response.getVariants() != null) {
            response.getVariants().forEach(variant -> {
                if (variant.getQuantity() != null && variant.getQuantity() > 0) {
                    variant.setInventoryStatus("Còn hàng");
                } else {
                    variant.setInventoryStatus("Hết hàng");
                }
            });
        }
    }

    @Override
    @BeanMapping(qualifiedByName = "attachProduct")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "brandId", target = "brand")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "unitId", target = "unit")
    @Mapping(source = "guaranteeId", target = "guarantee")
    Product requestToEntity(ProductRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachProduct")
    @Mapping(source = "categoryId", target = "category")
    @Mapping(source = "brandId", target = "brand")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "unitId", target = "unit")
    @Mapping(source = "guaranteeId", target = "guarantee")
    Product partialUpdate(@MappingTarget Product entity, ProductRequest request);

}

