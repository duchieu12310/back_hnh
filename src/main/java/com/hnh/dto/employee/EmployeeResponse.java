package com.hnh.dto.employee;

import com.hnh.dto.authentication.UserResponse;
import lombok.Data;

import java.time.Instant;

@Data
public class EmployeeResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    private UserResponse user;
    private OfficeResponse office;
    private DepartmentResponse department;
    private JobTypeResponse jobType;
    private JobLevelResponse jobLevel;
    private JobTitleResponse jobTitle;
}

