package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.UnitRequest;
import com.thatannhien.dto.product.UnitResponse;
import com.thatannhien.entity.product.Unit;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UnitMapper extends GenericMapper<Unit, UnitRequest, UnitResponse> {
}
