package com.hnh.mapper.warehouse;

import com.hnh.dto.warehouse.StorageLocationRequest;
import com.hnh.dto.warehouse.StorageLocationResponse;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class})
public interface StorageLocationMapper extends GenericMapper<StorageLocation, StorageLocationRequest, StorageLocationResponse> {

    @Override
    @Mapping(target = "inventoryItems", ignore = true) // Handled in service
    @Mapping(source = "warehouseId", target = "warehouse", qualifiedByName = "mapToWarehouse")
    StorageLocation requestToEntity(StorageLocationRequest request);

    @Override
    @Mapping(target = "inventoryItems", ignore = true) // Handled in service
    @Mapping(source = "warehouseId", target = "warehouse", qualifiedByName = "mapToWarehouse")
    StorageLocation partialUpdate(@MappingTarget StorageLocation entity, StorageLocationRequest request);

    @Override
    @Mapping(source = "warehouse.id", target = "warehouseId")
    @Mapping(source = "warehouse.name", target = "warehouseName")
    @Mapping(source = "inventoryItems", target = "items")
    StorageLocationResponse entityToResponse(StorageLocation entity);

    @Mapping(source = "variant.product.id", target = "productId")
    @Mapping(source = "variant.product.name", target = "productName")
    @Mapping(source = "variant.id", target = "variantId")
    @Mapping(source = "variant.sku", target = "sku")
    @Mapping(source = "quantity", target = "quantity")
    StorageLocationResponse.ItemDto inventoryItemToItemDto(com.hnh.entity.warehouse.InventoryItem inventoryItem);
}

