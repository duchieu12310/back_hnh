package com.hnh.service.warehouse;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.warehouse.WarehouseRequest;
import com.hnh.dto.warehouse.WarehouseResponse;
import com.hnh.mapper.warehouse.WarehouseMapper;
import com.hnh.repository.warehouse.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.entity.warehouse.InventoryItem;
import com.hnh.entity.warehouse.Warehouse;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final com.hnh.repository.warehouse.StorageLocationRepository storageLocationRepository;
    private final com.hnh.repository.warehouse.InventoryItemRepository inventoryItemRepository;

    @Override
    public ListResponse<WarehouseResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.WAREHOUSE, warehouseRepository, warehouseMapper);
    }

    @Override
    public WarehouseResponse findById(Long id) {
        return defaultFindById(id, warehouseRepository, warehouseMapper, ResourceName.WAREHOUSE);
    }

    @Override
    public WarehouseResponse save(WarehouseRequest request) {
        WarehouseResponse response = defaultSave(request, warehouseRepository, warehouseMapper);
        initInventoryItems(response.getId());
        return response;
    }

    @Override
    public WarehouseResponse save(Long id, WarehouseRequest request) {
        WarehouseResponse response = defaultSave(id, request, warehouseRepository, warehouseMapper, ResourceName.WAREHOUSE);
        initInventoryItems(id);
        return response;
    }

    private void initInventoryItems(Long warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId).orElse(null);
        if (warehouse == null) return;

        // 1. Get or create default StorageLocation (Null coordinates)
        StorageLocation defaultLocation = storageLocationRepository
                .findByWarehouseIdAndAisleAndShelfAndBin(warehouseId, null, null, null)
                .orElseGet(() -> storageLocationRepository.save(
                        new StorageLocation()
                                .setWarehouse(warehouse)
                                .setAisle(null)
                                .setShelf(null)
                                .setBin(null)
                ));

        // 2. Initialize InventoryItems for all Variants of assigned Products
        if (warehouse.getProducts() != null) {
            for (com.hnh.entity.product.Product product : warehouse.getProducts()) {
                if (product.getVariants() != null) {
                    for (com.hnh.entity.product.Variant variant : product.getVariants()) {
                        // Check if InventoryItem already exists in THIS location
                        if (inventoryItemRepository.findByVariantIdAndStorageLocationId(variant.getId(), defaultLocation.getId()).isEmpty()) {
                            inventoryItemRepository.save(
                                    new InventoryItem()
                                            .setVariant(variant)
                                            .setStorageLocation(defaultLocation)
                                            .setQuantity(0)
                            );
                        }
                    }
                }
            }
        }
    }

    @Override
    public void delete(Long id) {
        warehouseRepository.deleteById(id);
    }

    @Override
    public void delete(List<Long> ids) {
        warehouseRepository.deleteAllById(ids);
    }

    @Override
    public WarehouseResponse updateStatus(Long id, Integer status) {
        return warehouseRepository.findById(id)
                .map(entity -> {
                    entity.setStatus(status);
                    return warehouseRepository.save(entity);
                })
                .map(warehouseMapper::entityToResponse)
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException(ResourceName.WAREHOUSE, com.hnh.constant.FieldName.ID, id));
    }
}
