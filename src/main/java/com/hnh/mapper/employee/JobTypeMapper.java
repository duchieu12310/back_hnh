package com.hnh.mapper.employee;

import com.hnh.dto.employee.JobTypeRequest;
import com.hnh.dto.employee.JobTypeResponse;
import com.hnh.entity.employee.JobType;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobTypeMapper extends GenericMapper<JobType, JobTypeRequest, JobTypeResponse> {
}

