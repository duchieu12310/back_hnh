package com.hnh.mapper.order;

import com.hnh.dto.order.OrderRequest;
import com.hnh.dto.order.OrderResponse;
import com.hnh.entity.order.Order;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, OrderResourceMapper.class, OrderCancellationReasonMapper.class, UserMapper.class,
                OrderVariantMapper.class})
public interface OrderMapper extends GenericMapper<Order, OrderRequest, OrderResponse> {

    @Override
    @Mapping(source = "orderResourceId", target = "orderResource", qualifiedByName = "mapToOrderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason", qualifiedByName = "mapToOrderCancellationReason")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapToUser")
    Order requestToEntity(OrderRequest request);

    @Override
    @Mapping(source = "orderResourceId", target = "orderResource", qualifiedByName = "mapToOrderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason", qualifiedByName = "mapToOrderCancellationReason")
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapToUser")
    Order partialUpdate(@MappingTarget Order entity, OrderRequest request);
}
