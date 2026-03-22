package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.PropertyRequest;
import com.thatannhien.dto.product.PropertyResponse;
import com.thatannhien.entity.product.Property;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PropertyMapper extends GenericMapper<Property, PropertyRequest, PropertyResponse> {
}
