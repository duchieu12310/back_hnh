package com.hnh.mapper.authentication;

import com.hnh.dto.authentication.RoleRequest;
import com.hnh.dto.authentication.RoleResponse;
import com.hnh.entity.authentication.Role;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper extends GenericMapper<Role, RoleRequest, RoleResponse> {
}

