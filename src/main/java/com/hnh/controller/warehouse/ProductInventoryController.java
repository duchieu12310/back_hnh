package com.hnh.controller.warehouse;

import com.hnh.constant.AppConstants;
import com.hnh.dto.warehouse.CategoryLevel1Node;
import com.hnh.dto.warehouse.InventoryFilterRequest;
import com.hnh.dto.warehouse.AutoSaveInventoryRequest;
import com.hnh.service.warehouse.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-inventories")
@RequiredArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ProductInventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/hierarchy")
    public ResponseEntity<List<CategoryLevel1Node>> getHierarchicalInventory(
            @RequestParam(name = "warehouseId", required = false) Long warehouseId,
            @RequestParam(name = "aisle", required = false) String aisle,
            @RequestParam(name = "shelf", required = false) String shelf,
            @RequestParam(name = "bin", required = false) String bin,
            @RequestParam(name = "categoryL1Ids", required = false) List<Long> categoryL1Ids,
            @RequestParam(name = "categoryL2Ids", required = false) List<Long> categoryL2Ids,
            @RequestParam(name = "categoryL3Ids", required = false) List<Long> categoryL3Ids,
            @RequestParam(name = "productIds", required = false) List<Long> productIds
    ) {
        InventoryFilterRequest request = new InventoryFilterRequest();
        request.setWarehouseId(warehouseId);
        request.setAisle(aisle);
        request.setShelf(shelf);
        request.setBin(bin);
        request.setCategoryL1Ids(categoryL1Ids);
        request.setCategoryL2Ids(categoryL2Ids);
        request.setCategoryL3Ids(categoryL3Ids);
        request.setProductIds(productIds);
        return ResponseEntity.ok(inventoryService.getHierarchicalInventory(request));
    }

    @PostMapping("/auto-save")
    public ResponseEntity<Void> autoSaveInventory(@RequestBody AutoSaveInventoryRequest dto) {
        inventoryService.updateInventory(dto);
        return ResponseEntity.ok().build();
    }
}
