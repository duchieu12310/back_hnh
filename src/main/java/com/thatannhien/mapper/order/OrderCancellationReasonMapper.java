package com.thatannhien.mapper.order;

import com.thatannhien.dto.order.OrderCancellationReasonRequest;
import com.thatannhien.dto.order.OrderCancellationReasonResponse;
import com.thatannhien.entity.order.OrderCancellationReason;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrderCancellationReasonMapper extends GenericMapper<OrderCancellationReason, OrderCancellationReasonRequest,
        OrderCancellationReasonResponse> {}
