package com.thatannhien.mapper.customer;

import com.thatannhien.dto.customer.CustomerStatusRequest;
import com.thatannhien.dto.customer.CustomerStatusResponse;
import com.thatannhien.entity.customer.CustomerStatus;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerStatusMapper extends GenericMapper<CustomerStatus, CustomerStatusRequest, CustomerStatusResponse> {
}
