package com.thatannhien.mapper.order;


import com.thatannhien.dto.order.OrderResourceRequest;
import com.thatannhien.dto.order.OrderResourceResponse;
import com.thatannhien.entity.order.OrderResource;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.customer.CustomerResourceMapper;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, CustomerResourceMapper.class})
public interface OrderResourceMapper extends GenericMapper<OrderResource, OrderResourceRequest, OrderResourceResponse> {

    @Override
    @Mapping(source = "customerResourceId", target = "customerResource")
    OrderResource requestToEntity(OrderResourceRequest request);

    @Override
    @Mapping(source = "customerResourceId", target = "customerResource")
    OrderResource partialUpdate(@MappingTarget OrderResource entity, OrderResourceRequest request);

}
