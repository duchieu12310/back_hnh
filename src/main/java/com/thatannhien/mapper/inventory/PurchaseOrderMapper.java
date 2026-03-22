package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.PurchaseOrderRequest;
import com.thatannhien.dto.inventory.PurchaseOrderResponse;
import com.thatannhien.entity.inventory.PurchaseOrder;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.product.SupplierMapper;
import com.thatannhien.utils.MapperUtils;
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
