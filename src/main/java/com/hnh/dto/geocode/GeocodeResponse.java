package com.hnh.dto.geocode;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GeocodeResponse {
    private Double latitude;
    private Double longitude;
}
