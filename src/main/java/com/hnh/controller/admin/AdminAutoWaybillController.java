package com.hnh.controller.admin;

import com.hnh.service.waybill.AutoWaybillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/waybills/auto-waybill")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AdminAutoWaybillController {

    private final AutoWaybillService autoWaybillService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Boolean>> getStatus() {
        Map<String, Boolean> response = new HashMap<>();
        response.put("enabled", autoWaybillService.isAutoWaybillEnabled());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/toggle")
    public ResponseEntity<Map<String, String>> toggleAutoWaybill(@RequestParam("enabled") boolean enabled) {
        autoWaybillService.setAutoWaybillEnabled(enabled);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Auto Waybill feature is now " + (enabled ? "enabled" : "disabled"));
        return ResponseEntity.ok(response);
    }
}
