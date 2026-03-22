package com.thatannhien.mapper.employee;

import com.thatannhien.dto.employee.OfficeRequest;
import com.thatannhien.dto.employee.OfficeResponse;
import com.thatannhien.entity.employee.Office;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface OfficeMapper extends GenericMapper<Office, OfficeRequest, OfficeResponse> {
}
