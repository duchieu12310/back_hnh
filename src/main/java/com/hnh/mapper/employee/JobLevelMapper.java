package com.hnh.mapper.employee;

import com.hnh.dto.employee.JobLevelRequest;
import com.hnh.dto.employee.JobLevelResponse;
import com.hnh.entity.employee.JobLevel;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobLevelMapper extends GenericMapper<JobLevel, JobLevelRequest, JobLevelResponse> {
}

