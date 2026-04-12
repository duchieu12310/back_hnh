package com.hnh.mapper.employee;

import com.hnh.dto.employee.JobTitleRequest;
import com.hnh.dto.employee.JobTitleResponse;
import com.hnh.entity.employee.JobTitle;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobTitleMapper extends GenericMapper<JobTitle, JobTitleRequest, JobTitleResponse> {
}

