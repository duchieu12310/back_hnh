package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.GuaranteeRequest;
import com.thatannhien.dto.product.GuaranteeResponse;
import com.thatannhien.entity.product.Guarantee;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface GuaranteeMapper extends GenericMapper<Guarantee, GuaranteeRequest, GuaranteeResponse> {
}
