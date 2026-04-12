package com.hnh.controller.warehouse;

import com.hnh.constant.AppConstants;
import com.hnh.service.warehouse.StorageLocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/storage-locations")
@RequiredArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class StorageLocationController {

    private final StorageLocationService storageLocationService;

    @org.springframework.web.bind.annotation.GetMapping
    public org.springframework.http.ResponseEntity<com.hnh.dto.ListResponse<com.hnh.dto.warehouse.StorageLocationResponse>> getAllResources(
            @org.springframework.web.bind.annotation.RequestParam(name = "page", defaultValue = "1") int page,
            @org.springframework.web.bind.annotation.RequestParam(name = "size", defaultValue = "10") int size,
            @org.springframework.web.bind.annotation.RequestParam(name = "sort", defaultValue = "id,desc") String sort,
            @org.springframework.web.bind.annotation.RequestParam(name = "filter", required = false) String filter,
            @org.springframework.web.bind.annotation.RequestParam(name = "search", required = false) String search,
            @org.springframework.web.bind.annotation.RequestParam(name = "all", required = false) boolean all
    ) {
        return org.springframework.http.ResponseEntity.ok(storageLocationService.findAll(page, size, sort, filter, search, all));
    }

    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.StorageLocationResponse> getResource(@org.springframework.web.bind.annotation.PathVariable Long id) {
        return org.springframework.http.ResponseEntity.ok(storageLocationService.findById(id));
    }

    @org.springframework.web.bind.annotation.PostMapping
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.StorageLocationResponse> createResource(@org.springframework.web.bind.annotation.RequestBody com.hnh.dto.warehouse.StorageLocationRequest request) {
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(storageLocationService.save(request));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.StorageLocationResponse> updateResource(@org.springframework.web.bind.annotation.PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody com.hnh.dto.warehouse.StorageLocationRequest request) {
        return org.springframework.http.ResponseEntity.ok(storageLocationService.save(id, request));
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> deleteResource(@org.springframework.web.bind.annotation.PathVariable Long id) {
        storageLocationService.delete(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping
    public org.springframework.http.ResponseEntity<Void> deleteResources(@org.springframework.web.bind.annotation.RequestBody java.util.List<Long> ids) {
        storageLocationService.delete(ids);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/adjust-quantity")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.StorageLocationResponse> adjustQuantity(
            @org.springframework.web.bind.annotation.PathVariable Long id,
            @org.springframework.web.bind.annotation.RequestBody com.fasterxml.jackson.databind.JsonNode request
    ) {
        int adjustment = request.get("adjustment").asInt();
        return org.springframework.http.ResponseEntity.ok(storageLocationService.adjustQuantity(id, adjustment));
    }
    @org.springframework.web.bind.annotation.PutMapping("/{id}/clear-quantity")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.StorageLocationResponse> clearQuantity(@org.springframework.web.bind.annotation.PathVariable Long id) {
        return org.springframework.http.ResponseEntity.ok(storageLocationService.clearQuantity(id));
    }
}
