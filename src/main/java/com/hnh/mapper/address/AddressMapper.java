package com.hnh.mapper.address;

import com.hnh.dto.address.AddressRequest;
import com.hnh.dto.address.AddressResponse;
import com.hnh.entity.address.Address;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface AddressMapper extends GenericMapper<Address, AddressRequest, AddressResponse> {

    @Override
    @Mapping(source = "provinceId", target = "province", qualifiedByName = "mapToProvince")
    @Mapping(source = "districtId", target = "district", qualifiedByName = "mapToDistrict")
    @Mapping(source = "wardId", target = "ward", qualifiedByName = "mapToWard")
    Address requestToEntity(AddressRequest request);

    @Override
    @Mapping(source = "provinceId", target = "province", qualifiedByName = "mapToProvince")
    @Mapping(source = "districtId", target = "district", qualifiedByName = "mapToDistrict")
    @Mapping(source = "wardId", target = "ward", qualifiedByName = "mapToWard")
    Address partialUpdate(@MappingTarget Address entity, AddressRequest request);

}
