package com.hnh.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WardRequest {
    private String name;
    private String code;
    private Long districtId;
}
