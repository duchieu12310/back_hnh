package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.DocketRequest;
import com.hnh.dto.inventory.DocketResponse;
import com.hnh.entity.inventory.Docket;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, DocketReasonMapper.class, WarehouseMapper.class, DocketVariantMapper.class})
public interface DocketMapper extends GenericMapper<Docket, DocketRequest, DocketResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachDocket")
    @Mapping(source = "reasonId", target = "reason")
    @Mapping(source = "warehouseId", target = "warehouse")
    @Mapping(source = "purchaseOrderId", target = "purchaseOrder")
    @Mapping(source = "orderId", target = "order")
    Docket requestToEntity(DocketRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachDocket")
    @Mapping(source = "reasonId", target = "reason")
    @Mapping(source = "warehouseId", target = "warehouse")
    @Mapping(source = "purchaseOrderId", target = "purchaseOrder")
    @Mapping(source = "orderId", target = "order")
    Docket partialUpdate(@MappingTarget Docket entity, DocketRequest request);

}

