package com.thatannhien.mapper.address;

import com.thatannhien.dto.address.ProvinceRequest;
import com.thatannhien.dto.address.ProvinceResponse;
import com.thatannhien.entity.address.Province;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProvinceMapper extends GenericMapper<Province, ProvinceRequest, ProvinceResponse> {
}
