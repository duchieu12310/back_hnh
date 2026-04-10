package com.hnh.mapper.customer;

import com.hnh.dto.customer.CustomerRequest;
import com.hnh.dto.customer.CustomerResponse;
import com.hnh.entity.customer.Customer;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class, UserMapper.class})
public interface CustomerMapper extends GenericMapper<Customer, CustomerRequest, CustomerResponse> {

    @Override
    @Mapping(source = "customerGroupId", target = "customerGroup", qualifiedByName = "mapToCustomerGroup")
    @Mapping(source = "customerResourceId", target = "customerResource", qualifiedByName = "mapToCustomerResource")
    @Mapping(source = "customerStatusId", target = "customerStatus", qualifiedByName = "mapToCustomerStatus")
    Customer requestToEntity(CustomerRequest request);

    @Override
    @Mapping(source = "customerGroupId", target = "customerGroup", qualifiedByName = "mapToCustomerGroup")
    @Mapping(source = "customerResourceId", target = "customerResource", qualifiedByName = "mapToCustomerResource")
    @Mapping(source = "customerStatusId", target = "customerStatus", qualifiedByName = "mapToCustomerStatus")
    Customer partialUpdate(@MappingTarget Customer entity, CustomerRequest request);

}
