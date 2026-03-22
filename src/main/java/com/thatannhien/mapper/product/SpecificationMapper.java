package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.SpecificationRequest;
import com.thatannhien.dto.product.SpecificationResponse;
import com.thatannhien.entity.product.Specification;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SpecificationMapper extends GenericMapper<Specification, SpecificationRequest, SpecificationResponse> {
}
