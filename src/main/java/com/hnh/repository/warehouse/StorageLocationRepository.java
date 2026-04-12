package com.hnh.repository.warehouse;

import com.hnh.entity.warehouse.StorageLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface StorageLocationRepository extends JpaRepository<StorageLocation, Long>, JpaSpecificationExecutor<StorageLocation> {
    Optional<StorageLocation> findByWarehouseIdAndAisleAndShelfAndBin(Long warehouseId, String aisle, String shelf, String bin);
}

