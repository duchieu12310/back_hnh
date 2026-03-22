package com.thatannhien.mapper.waybill;

import com.thatannhien.dto.waybill.WaybillRequest;
import com.thatannhien.dto.waybill.WaybillResponse;
import com.thatannhien.entity.waybill.Waybill;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.order.OrderMapper;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class, OrderMapper.class})
public interface WaybillMapper extends GenericMapper<Waybill, WaybillRequest, WaybillResponse> {

    @Override
    @Mapping(source = "orderId", target = "order")
    Waybill requestToEntity(WaybillRequest request);

    @Override
    @Mapping(source = "orderId", target = "order")
    Waybill partialUpdate(@MappingTarget Waybill entity, WaybillRequest request);

}
