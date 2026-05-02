package com.hnh.dto.address;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressResponse {
    private Long id;
    private Instant createdAt;
    private Instant updatedAt;
    @Nullable
    private String line;
    @Nullable
    private ProvinceResponse province;
    @Nullable
    private DistrictResponse district;
    @Nullable
    private WardResponse ward;
    @Nullable
    private Double latitude;
    @Nullable
    private Double longitude;
    private Boolean isDefault;
}
