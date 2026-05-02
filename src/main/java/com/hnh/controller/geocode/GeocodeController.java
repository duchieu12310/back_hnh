package com.hnh.controller.geocode;

import com.hnh.dto.geocode.GeocodeResponse;
import com.hnh.service.geocode.GeocodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@RequestMapping("/api/geocode")
@CrossOrigin("*")
public class GeocodeController {

    private final GeocodeService geocodeService;

    public GeocodeController(GeocodeService geocodeService) {
        this.geocodeService = geocodeService;
    }

    @GetMapping
    public ResponseEntity<GeocodeResponse> getCoordinates(
            @RequestParam Long provinceId,
            @RequestParam Long districtId,
            @RequestParam(required = false) Long wardId) {

        GeocodeResponse resp = geocodeService.getCoordinates(provinceId, districtId, wardId);
        return ResponseEntity.ok(resp);
    }
}
