package com.hnh.mapper.customer;

import com.hnh.dto.customer.CustomerStatusRequest;
import com.hnh.dto.customer.CustomerStatusResponse;
import com.hnh.entity.customer.CustomerStatus;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerStatusMapper extends GenericMapper<CustomerStatus, CustomerStatusRequest, CustomerStatusResponse> {
}

