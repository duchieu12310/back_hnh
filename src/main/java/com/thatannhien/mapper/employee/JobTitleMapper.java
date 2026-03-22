package com.thatannhien.mapper.employee;

import com.thatannhien.dto.employee.JobTitleRequest;
import com.thatannhien.dto.employee.JobTitleResponse;
import com.thatannhien.entity.employee.JobTitle;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface JobTitleMapper extends GenericMapper<JobTitle, JobTitleRequest, JobTitleResponse> {
}
