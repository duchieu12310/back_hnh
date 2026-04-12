package com.hnh.mapper.customer;

import com.hnh.dto.customer.CustomerResourceRequest;
import com.hnh.dto.customer.CustomerResourceResponse;
import com.hnh.entity.customer.CustomerResource;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerResourceMapper extends GenericMapper<CustomerResource, CustomerResourceRequest, CustomerResourceResponse> {
}

