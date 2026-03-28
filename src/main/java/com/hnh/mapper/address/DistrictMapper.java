package com.hnh.mapper.address;

import com.hnh.dto.address.DistrictRequest;
import com.hnh.dto.address.DistrictResponse;
import com.hnh.entity.address.District;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface DistrictMapper extends GenericMapper<District, DistrictRequest, DistrictResponse> {

    @Override
    @Mapping(source = "provinceId", target = "province")
    District requestToEntity(DistrictRequest request);

    @Override
    @Mapping(source = "provinceId", target = "province")
    District partialUpdate(@MappingTarget District entity, DistrictRequest request);

}

