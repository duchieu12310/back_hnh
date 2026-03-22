package com.thatannhien.mapper.employee;

import com.thatannhien.dto.employee.JobTypeRequest;
import com.thatannhien.dto.employee.JobTypeResponse;
import com.thatannhien.entity.employee.JobType;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobTypeMapper extends GenericMapper<JobType, JobTypeRequest, JobTypeResponse> {
}
