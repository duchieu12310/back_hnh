package com.hnh.mapper.order;


import com.hnh.dto.order.OrderRequest;
import com.hnh.dto.order.OrderResponse;
import com.hnh.entity.order.Order;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, OrderResourceMapper.class, OrderCancellationReasonMapper.class, UserMapper.class,
                OrderVariantMapper.class})
public interface OrderMapper extends GenericMapper<Order, OrderRequest, OrderResponse> {

    @Override
    @BeanMapping(qualifiedByName = "attachOrder")
    @Mapping(source = "orderResourceId", target = "orderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason")
    @Mapping(source = "userId", target = "user")
    Order requestToEntity(OrderRequest request);

    @Override
    @BeanMapping(qualifiedByName = "attachOrder")
    @Mapping(source = "orderResourceId", target = "orderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason")
    @Mapping(source = "userId", target = "user")
    Order partialUpdate(@MappingTarget Order entity, OrderRequest request);

}

