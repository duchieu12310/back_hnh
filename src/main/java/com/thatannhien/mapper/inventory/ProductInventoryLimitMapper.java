package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.ProductInventoryLimitRequest;
import com.thatannhien.dto.inventory.ProductInventoryLimitResponse;
import com.thatannhien.entity.inventory.ProductInventoryLimit;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.utils.MapperUtils;
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
