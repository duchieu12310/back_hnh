package com.thatannhien.mapper.employee;

import com.thatannhien.dto.employee.JobLevelRequest;
import com.thatannhien.dto.employee.JobLevelResponse;
import com.thatannhien.entity.employee.JobLevel;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobLevelMapper extends GenericMapper<JobLevel, JobLevelRequest, JobLevelResponse> {
}
