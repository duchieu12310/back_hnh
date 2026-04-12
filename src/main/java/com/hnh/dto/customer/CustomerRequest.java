package com.hnh.dto.customer;

import com.hnh.dto.authentication.UserRequest;
import lombok.Data;

@Data
public class CustomerRequest {
    private UserRequest user;
    private Long customerGroupId;
    private Long customerStatusId;
    private Long customerResourceId;
}

