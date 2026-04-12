package com.hnh.mapper.product;

import com.hnh.dto.product.PropertyRequest;
import com.hnh.dto.product.PropertyResponse;
import com.hnh.entity.product.Property;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper extends GenericMapper<Property, PropertyRequest, PropertyResponse> {
}

