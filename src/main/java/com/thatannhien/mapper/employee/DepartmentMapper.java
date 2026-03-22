package com.thatannhien.mapper.employee;

import com.thatannhien.dto.employee.DepartmentRequest;
import com.thatannhien.dto.employee.DepartmentResponse;
import com.thatannhien.entity.employee.Department;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DepartmentMapper extends GenericMapper<Department, DepartmentRequest, DepartmentResponse> {
}
