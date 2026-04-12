package com.hnh.mapper.customer;

import com.hnh.dto.customer.CustomerGroupRequest;
import com.hnh.dto.customer.CustomerGroupResponse;
import com.hnh.entity.customer.CustomerGroup;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerGroupMapper extends GenericMapper<CustomerGroup, CustomerGroupRequest, CustomerGroupResponse> {
}

