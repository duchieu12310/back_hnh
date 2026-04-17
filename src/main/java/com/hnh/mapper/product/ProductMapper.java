package com.hnh.mapper.product;

import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.entity.product.Product;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.general.ImageMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
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

    @AfterMapping
    default void syncRelationships(@MappingTarget Product entity) {
        if (entity.getVariants() != null) {
            entity.getVariants().forEach(variant -> variant.setProduct(entity));
        }
        if (entity.getImages() != null) {
            entity.getImages().forEach(image -> image.setProduct(entity));
        }
    }

    @Override
    @Mapping(source = "categoryIds", target = "categories", qualifiedByName = "mapToCategories")
    @Mapping(source = "brandId", target = "brand", qualifiedByName = "mapToBrand")
    @Mapping(source = "supplierId", target = "supplier", qualifiedByName = "mapToSupplier")
    @Mapping(source = "unitId", target = "unit", qualifiedByName = "mapToUnit")
    @Mapping(source = "guaranteeId", target = "guarantee", qualifiedByName = "mapToGuarantee")
    Product requestToEntity(ProductRequest request);

    @Override
    @Mapping(source = "categoryIds", target = "categories", qualifiedByName = "mapToCategories")
    @Mapping(source = "brandId", target = "brand", qualifiedByName = "mapToBrand")
    @Mapping(source = "supplierId", target = "supplier", qualifiedByName = "mapToSupplier")
    @Mapping(source = "unitId", target = "unit", qualifiedByName = "mapToUnit")
    @Mapping(source = "guaranteeId", target = "guarantee", qualifiedByName = "mapToGuarantee")
    Product partialUpdate(@MappingTarget Product entity, ProductRequest request);

}
