package com.hnh.mapper.product;

import com.hnh.dto.product.GuaranteeRequest;
import com.hnh.dto.product.GuaranteeResponse;
import com.hnh.entity.product.Guarantee;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuaranteeMapper extends GenericMapper<Guarantee, GuaranteeRequest, GuaranteeResponse> {
}

