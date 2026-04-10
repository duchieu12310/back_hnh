package com.hnh.mapper.employee;

import com.hnh.dto.employee.EmployeeRequest;
import com.hnh.dto.employee.EmployeeResponse;
import com.hnh.entity.employee.Employee;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.authentication.UserMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {MapperUtils.class, UserMapper.class})
public interface EmployeeMapper extends GenericMapper<Employee, EmployeeRequest, EmployeeResponse> {

    @Override
    @Mapping(source = "officeId", target = "office", qualifiedByName = "mapToOffice")
    @Mapping(source = "departmentId", target = "department", qualifiedByName = "mapToDepartment")
    @Mapping(source = "jobTypeId", target = "jobType", qualifiedByName = "mapToJobType")
    @Mapping(source = "jobLevelId", target = "jobLevel", qualifiedByName = "mapToJobLevel")
    @Mapping(source = "jobTitleId", target = "jobTitle", qualifiedByName = "mapToJobTitle")
    Employee requestToEntity(EmployeeRequest request);

    @Override
    @Mapping(source = "officeId", target = "office", qualifiedByName = "mapToOffice")
    @Mapping(source = "departmentId", target = "department", qualifiedByName = "mapToDepartment")
    @Mapping(source = "jobTypeId", target = "jobType", qualifiedByName = "mapToJobType")
    @Mapping(source = "jobLevelId", target = "jobLevel", qualifiedByName = "mapToJobLevel")
    @Mapping(source = "jobTitleId", target = "jobTitle", qualifiedByName = "mapToJobTitle")
    Employee partialUpdate(@MappingTarget Employee entity, EmployeeRequest request);

}
