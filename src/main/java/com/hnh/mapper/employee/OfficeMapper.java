package com.hnh.mapper.employee;

import com.hnh.dto.employee.OfficeRequest;
import com.hnh.dto.employee.OfficeResponse;
import com.hnh.entity.employee.Office;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface OfficeMapper extends GenericMapper<Office, OfficeRequest, OfficeResponse> {
}

