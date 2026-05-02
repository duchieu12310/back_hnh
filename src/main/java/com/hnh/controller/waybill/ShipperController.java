package com.hnh.controller.waybill;

import com.hnh.constant.AppConstants;
import com.hnh.dto.waybill.WaybillResponse;
import com.hnh.service.shipper.ShipperService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/shipper")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ShipperController {

    private final ShipperService shipperService;

    @GetMapping("/waybills/available")
    public ResponseEntity<List<WaybillResponse>> getAvailableWaybills(
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lng) {
        return ResponseEntity.ok(shipperService.getAvailableWaybills(lat, lng));
    }

    @PostMapping("/waybills/{id}/confirm-pickup")
    public ResponseEntity<WaybillResponse> confirmPickup(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(shipperService.confirmPickup(id, userDetails.getUsername()));
    }
}
