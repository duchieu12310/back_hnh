package com.hnh.service.warehouse;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.ListResponse;
import com.hnh.dto.warehouse.StorageLocationRequest;
import com.hnh.dto.warehouse.StorageLocationResponse;
import com.hnh.mapper.warehouse.StorageLocationMapper;
import com.hnh.repository.product.VariantRepository;
import com.hnh.entity.product.Variant;
import com.hnh.entity.warehouse.InventoryItem;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.constant.FieldName;
import com.hnh.repository.warehouse.InventoryItemRepository;
import com.hnh.repository.warehouse.StorageLocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class StorageLocationServiceImpl implements StorageLocationService {

    private final StorageLocationRepository storageLocationRepository;
    private final StorageLocationMapper storageLocationMapper;
    private final VariantRepository variantRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @Override
    public ListResponse<StorageLocationResponse> findAll(int page, int size, String sort, String filter, String search, boolean all) {
        // Handle custom filtering for categoryIds and productIds if needed through the search logic
        // For now, we keep the default behavior but we might need to update SearchFields or Specifications
        return defaultFindAll(page, size, sort, filter, search, all, SearchFields.STORAGE_LOCATION, storageLocationRepository, storageLocationMapper);
    }

    @Override
    public StorageLocationResponse findById(Long id) {
        return defaultFindById(id, storageLocationRepository, storageLocationMapper, ResourceName.STORAGE_LOCATION);
    }

    @Override
    public StorageLocationResponse save(StorageLocationRequest request) {
        return processStorageLocationRequest(null, request);
    }

    @Override
    public StorageLocationResponse save(Long id, StorageLocationRequest request) {
        return processStorageLocationRequest(id, request);
    }

    private StorageLocationResponse processStorageLocationRequest(Long id, StorageLocationRequest request) {
        StorageLocation entity;
        if (id != null) {
            entity = storageLocationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException(ResourceName.STORAGE_LOCATION, FieldName.ID, id));
        } else {
            entity = new StorageLocation();
        }

        entity.setAisle(request.getAisle());
        entity.setShelf(request.getShelf());
        entity.setBin(request.getBin());

        if (request.getWarehouseId() != null) {
            com.hnh.entity.warehouse.Warehouse wh = new com.hnh.entity.warehouse.Warehouse();
            wh.setId(request.getWarehouseId());
            entity.setWarehouse(wh);
        }

        entity = storageLocationRepository.save(entity);

        if (request.getItems() != null) {
            for (StorageLocationRequest.ItemDto itemDto : request.getItems()) {
                Variant variant = variantRepository.findById(itemDto.getVariantId())
                        .orElseThrow(() -> new RuntimeException("Variant not found: " + itemDto.getVariantId()));

                InventoryItem item = inventoryItemRepository.findByVariantIdAndStorageLocationId(variant.getId(), entity.getId())
                        .orElse(new InventoryItem().setVariant(variant).setStorageLocation(entity));

                if (itemDto.getQuantity() != null) {
                    item.setQuantity(itemDto.getQuantity());
                }
                inventoryItemRepository.save(item);
            }
        }

        return storageLocationMapper.entityToResponse(entity);
    }

    @Override
    public void delete(Long id) {
        StorageLocation location = storageLocationRepository.findById(id).orElse(null);
        if (location != null) {
            // InventoryItems will be cascade deleted because of orphanRemoval = true in StorageLocation
            storageLocationRepository.delete(location);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        ids.forEach(this::delete);
    }

    @Override
    public StorageLocationResponse updateStatus(Long id, Integer status) {
        throw new UnsupportedOperationException("StorageLocation does not support status update");
    }

    @Override
    public StorageLocationResponse adjustQuantity(Long id, Integer adjustment) {
        StorageLocation location = storageLocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.STORAGE_LOCATION, FieldName.ID, id));

        // If there is exactly one item, we can adjust it
        if (location.getInventoryItems().size() == 1) {
            InventoryItem item = location.getInventoryItems().get(0);
            int newQuantity = (item.getQuantity() != null ? item.getQuantity() : 0) + adjustment;
            if (newQuantity < 0) throw new RuntimeException("Quantity cannot be negative");
            item.setQuantity(newQuantity);
            inventoryItemRepository.save(item);

            return storageLocationMapper.entityToResponse(location);
        }
        throw new UnsupportedOperationException("Please use variant-specific adjustment for multi-item locations");
    }

    @Override
    public StorageLocationResponse clearQuantity(Long id) {
        StorageLocation location = storageLocationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.STORAGE_LOCATION, FieldName.ID, id));

        if (location.getInventoryItems().size() == 1) {
            InventoryItem item = location.getInventoryItems().get(0);
            item.setQuantity(0);
            inventoryItemRepository.save(item);
            return storageLocationMapper.entityToResponse(location);
        }
        throw new UnsupportedOperationException("Please use variant-specific clear for multi-item locations");
    }
}

