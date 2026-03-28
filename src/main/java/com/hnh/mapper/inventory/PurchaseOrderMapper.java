package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.PurchaseOrderRequest;
import com.hnh.dto.inventory.PurchaseOrderResponse;
import com.hnh.entity.inventory.PurchaseOrder;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.product.SupplierMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, SupplierMapper.class, DestinationMapper.class, PurchaseOrderVariantMapper.class})
public interface PurchaseOrderMapper extends GenericMapper<PurchaseOrder, PurchaseOrderRequest, PurchaseOrderResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachPurchaseOrder")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "destinationId", target = "destination")
    PurchaseOrder requestToEntity(PurchaseOrderRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachPurchaseOrder")
    @Mapping(source = "supplierId", target = "supplier")
    @Mapping(source = "destinationId", target = "destination")
    PurchaseOrder partialUpdate(@MappingTarget PurchaseOrder entity, PurchaseOrderRequest request);

}

