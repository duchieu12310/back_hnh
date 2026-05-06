package com.hnh.controller.authentication;

import com.hnh.constant.AppConstants;
import com.hnh.service.auth.UserManagementService;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/admin/users")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class UserManagementController {

    private UserManagementService userManagementService;

    @PutMapping("/{userId}/approve")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ObjectNode> approveUser(@PathVariable Long userId) {
        userManagementService.approveUser(userId);
        return ResponseEntity.ok(new ObjectNode(JsonNodeFactory.instance));
    }

    @PutMapping("/{userId}/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ObjectNode> rejectUser(@PathVariable Long userId, @RequestBody Map<String, String> request) {
        String reason = request.getOrDefault("reason", "Không có lý do cụ thể.");
        userManagementService.rejectUser(userId, reason);
        return ResponseEntity.ok(new ObjectNode(JsonNodeFactory.instance));
    }
}
