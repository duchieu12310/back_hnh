package com.thatannhien.mapper.authentication;

import com.thatannhien.dto.authentication.RoleRequest;
import com.thatannhien.dto.authentication.RoleResponse;
import com.thatannhien.entity.authentication.Role;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleRequest, RoleResponse> {
}
