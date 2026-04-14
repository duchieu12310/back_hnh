package com.hnh.repository.warehouse;

import com.hnh.entity.warehouse.InventoryItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long>, JpaSpecificationExecutor<InventoryItem> {
    Optional<InventoryItem> findByVariantIdAndStorageLocationId(Long variantId, Long storageLocationId);

    java.util.List<InventoryItem> findByVariantId(Long variantId);

    @org.springframework.data.jpa.repository.Query("SELECT ii FROM InventoryItem ii WHERE ii.variant.id = :variantId AND ii.storageLocation.warehouse.id = :warehouseId")
    java.util.List<InventoryItem> findByVariantIdAndWarehouseId(Long variantId, Long warehouseId);
}
