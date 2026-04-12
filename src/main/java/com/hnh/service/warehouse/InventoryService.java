package com.hnh.service.warehouse;

import com.hnh.dto.warehouse.CategoryLevel1Node;
import com.hnh.dto.warehouse.InventoryFilterRequest;
import com.hnh.dto.warehouse.AutoSaveInventoryRequest;

import java.util.List;

public interface InventoryService {
    List<CategoryLevel1Node> getHierarchicalInventory(InventoryFilterRequest request);
    void updateInventory(AutoSaveInventoryRequest dto);
}
