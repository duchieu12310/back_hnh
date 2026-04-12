package com.hnh.mapper.product;

import com.hnh.dto.product.SpecificationRequest;
import com.hnh.dto.product.SpecificationResponse;
import com.hnh.entity.product.Specification;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecificationMapper extends GenericMapper<Specification, SpecificationRequest, SpecificationResponse> {
}

