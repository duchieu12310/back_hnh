package com.hnh.mapper.address;

import com.hnh.dto.address.ProvinceRequest;
import com.hnh.dto.address.ProvinceResponse;
import com.hnh.entity.address.Province;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProvinceMapper extends GenericMapper<Province, ProvinceRequest, ProvinceResponse> {
}
