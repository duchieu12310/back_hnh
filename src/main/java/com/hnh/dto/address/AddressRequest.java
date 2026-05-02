package com.hnh.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressRequest {
    private String line;
    private Long provinceId;
    private Long districtId;
    private Long wardId;
    private Double latitude;
    private Double longitude;
}
