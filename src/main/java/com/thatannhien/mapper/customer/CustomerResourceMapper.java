package com.thatannhien.mapper.customer;

import com.thatannhien.dto.customer.CustomerResourceRequest;
import com.thatannhien.dto.customer.CustomerResourceResponse;
import com.thatannhien.entity.customer.CustomerResource;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerResourceMapper extends GenericMapper<CustomerResource, CustomerResourceRequest, CustomerResourceResponse> {
}
