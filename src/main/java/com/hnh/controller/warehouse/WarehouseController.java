package com.hnh.controller.warehouse;

import com.hnh.constant.AppConstants;
import com.hnh.service.warehouse.WarehouseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/warehouses")
@RequiredArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class WarehouseController {

    private final WarehouseService warehouseService;

    @org.springframework.web.bind.annotation.GetMapping
    public org.springframework.http.ResponseEntity<com.hnh.dto.ListResponse<com.hnh.dto.warehouse.WarehouseResponse>> getAllResources(
            @org.springframework.web.bind.annotation.RequestParam(name = "page", defaultValue = "1") int page,
            @org.springframework.web.bind.annotation.RequestParam(name = "size", defaultValue = "10") int size,
            @org.springframework.web.bind.annotation.RequestParam(name = "sort", defaultValue = "id,desc") String sort,
            @org.springframework.web.bind.annotation.RequestParam(name = "filter", required = false) String filter,
            @org.springframework.web.bind.annotation.RequestParam(name = "search", required = false) String search,
            @org.springframework.web.bind.annotation.RequestParam(name = "all", required = false) boolean all
    ) {
        return org.springframework.http.ResponseEntity.ok(warehouseService.findAll(page, size, sort, filter, search, all));
    }

    @org.springframework.web.bind.annotation.GetMapping("/{id}")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.WarehouseResponse> getResource(@org.springframework.web.bind.annotation.PathVariable Long id) {
        return org.springframework.http.ResponseEntity.ok(warehouseService.findById(id));
    }

    @org.springframework.web.bind.annotation.PostMapping
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.WarehouseResponse> createResource(@org.springframework.web.bind.annotation.RequestBody com.hnh.dto.warehouse.WarehouseRequest request) {
        return org.springframework.http.ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(warehouseService.save(request));
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.WarehouseResponse> updateResource(@org.springframework.web.bind.annotation.PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody com.hnh.dto.warehouse.WarehouseRequest request) {
        return org.springframework.http.ResponseEntity.ok(warehouseService.save(id, request));
    }

    @org.springframework.web.bind.annotation.DeleteMapping("/{id}")
    public org.springframework.http.ResponseEntity<Void> deleteResource(@org.springframework.web.bind.annotation.PathVariable Long id) {
        warehouseService.delete(id);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.DeleteMapping
    public org.springframework.http.ResponseEntity<Void> deleteResources(@org.springframework.web.bind.annotation.RequestBody java.util.List<Long> ids) {
        warehouseService.delete(ids);
        return org.springframework.http.ResponseEntity.noContent().build();
    }

    @org.springframework.web.bind.annotation.PutMapping("/{id}/status")
    public org.springframework.http.ResponseEntity<com.hnh.dto.warehouse.WarehouseResponse> updateStatus(@org.springframework.web.bind.annotation.PathVariable Long id, @org.springframework.web.bind.annotation.RequestBody com.fasterxml.jackson.databind.JsonNode request) {
        int status = request.get("status").asInt();
        return org.springframework.http.ResponseEntity.ok(warehouseService.updateStatus(id, status));
    }
}
