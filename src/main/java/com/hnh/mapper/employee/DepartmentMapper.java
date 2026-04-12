package com.hnh.mapper.employee;

import com.hnh.dto.employee.DepartmentRequest;
import com.hnh.dto.employee.DepartmentResponse;
import com.hnh.entity.employee.Department;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper extends GenericMapper<Department, DepartmentRequest, DepartmentResponse> {
}

