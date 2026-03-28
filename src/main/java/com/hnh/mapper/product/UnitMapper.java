package com.hnh.mapper.product;

import com.hnh.dto.product.UnitRequest;
import com.hnh.dto.product.UnitResponse;
import com.hnh.entity.product.Unit;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper extends GenericMapper<Unit, UnitRequest, UnitResponse> {
}

