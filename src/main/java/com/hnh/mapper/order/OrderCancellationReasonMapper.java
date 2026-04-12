package com.hnh.mapper.order;

import com.hnh.dto.order.OrderCancellationReasonRequest;
import com.hnh.dto.order.OrderCancellationReasonResponse;
import com.hnh.entity.order.OrderCancellationReason;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCancellationReasonMapper extends GenericMapper<OrderCancellationReason, OrderCancellationReasonRequest,
        OrderCancellationReasonResponse> {}

