package com.hnh.mapper.waybill;

import com.hnh.dto.waybill.WaybillRequest;
import com.hnh.dto.waybill.WaybillResponse;
import com.hnh.entity.waybill.Waybill;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.order.OrderMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.mapper.warehouse.WarehouseMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, 
        uses = {MapperUtils.class, OrderMapper.class, WarehouseMapper.class, UserMapper.class})
public abstract class WaybillMapper implements GenericMapper<Waybill, WaybillRequest, WaybillResponse> {

    @Override
    @Mapping(source = "orderId", target = "order", qualifiedByName = "mapToOrder")
    public abstract Waybill requestToEntity(WaybillRequest request);

    @Override
    @Mapping(source = "orderId", target = "order", qualifiedByName = "mapToOrder")
    public abstract Waybill partialUpdate(@MappingTarget Waybill entity, WaybillRequest request);
}
