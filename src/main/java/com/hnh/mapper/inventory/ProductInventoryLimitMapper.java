package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.ProductInventoryLimitRequest;
import com.hnh.dto.inventory.ProductInventoryLimitResponse;
import com.hnh.entity.inventory.ProductInventoryLimit;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface ProductInventoryLimitMapper extends GenericMapper<ProductInventoryLimit, ProductInventoryLimitRequest,
        ProductInventoryLimitResponse> {

    @Override
    @Mapping(source = "productId", target = "product")
    ProductInventoryLimit requestToEntity(ProductInventoryLimitRequest request);

    @Override
    @Mapping(source = "productId", target = "product")
    ProductInventoryLimit partialUpdate(@MappingTarget ProductInventoryLimit entity, ProductInventoryLimitRequest request);

}

