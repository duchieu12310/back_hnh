package com.thatannhien.mapper.customer;

import com.thatannhien.dto.customer.CustomerGroupRequest;
import com.thatannhien.dto.customer.CustomerGroupResponse;
import com.thatannhien.entity.customer.CustomerGroup;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CustomerGroupMapper extends GenericMapper<CustomerGroup, CustomerGroupRequest, CustomerGroupResponse> {
}
