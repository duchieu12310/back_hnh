package com.hnh.mapper.warehouse;

import com.hnh.dto.warehouse.WarehouseRequest;
import com.hnh.dto.warehouse.WarehouseResponse;
import com.hnh.entity.warehouse.Warehouse;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.address.AddressMapper;
import com.hnh.mapper.product.CategoryMapper;
import com.hnh.mapper.product.ProductMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@Mapper(
    componentModel = "spring", 
    unmappedTargetPolicy = ReportingPolicy.IGNORE, 
    uses = {MapperUtils.class, AddressMapper.class}
)
public abstract class WarehouseMapper implements GenericMapper<Warehouse, WarehouseRequest, WarehouseResponse> {

    @Autowired
    protected MapperUtils utils;

    @Override
    @Mapping(source = "address", target = "address", qualifiedByName = "mapToAddressFromDto")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapToCategoriesFromDto")
    @Mapping(target = "products", ignore = true)
    public abstract Warehouse requestToEntity(WarehouseRequest request);

    @Override
    @Mapping(source = "address", target = "address", qualifiedByName = "mapToAddressFromDto")
    @Mapping(source = "categories", target = "categories", qualifiedByName = "mapToCategoriesFromDto")
    @Mapping(target = "products", ignore = true)
    public abstract Warehouse partialUpdate(@MappingTarget Warehouse entity, WarehouseRequest request);

    @AfterMapping
    protected void mapProducts(@MappingTarget Warehouse warehouse, WarehouseRequest request) {
        if (request.getCategories() != null) {
            java.util.Set<Long> allProductIds = request.getCategories().stream()
                    .filter(c -> c.getProductIds() != null)
                    .flatMap(c -> c.getProductIds().stream())
                    .collect(Collectors.toSet());
            
            if (!allProductIds.isEmpty()) {
                warehouse.setProducts(utils.mapToProducts(allProductIds));
            }
        }
    }
}
