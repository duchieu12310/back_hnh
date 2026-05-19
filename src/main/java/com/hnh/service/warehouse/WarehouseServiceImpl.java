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
import com.hnh.entity.product.Variant;
import com.hnh.entity.product.Product;
import com.hnh.repository.product.VariantRepository;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Transactional
public class WarehouseServiceImpl implements WarehouseService {

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;
    private final com.hnh.repository.warehouse.StorageLocationRepository storageLocationRepository;
    private final com.hnh.repository.warehouse.InventoryItemRepository inventoryItemRepository;
    private final VariantRepository variantRepository;
    private final com.hnh.repository.product.ProductRepository productRepository;

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
        Warehouse existingWarehouse = warehouseRepository.findById(id).orElse(null);
        if (existingWarehouse != null) {
            // Find products that are currently assigned but NOT in the new request
            Set<Long> newProductIds = new HashSet<>();
            if (request.getCategories() != null) {
                for (WarehouseRequest.CategorySelectionDto catDto : request.getCategories()) {
                    if (catDto.getProductIds() != null) {
                        newProductIds.addAll(catDto.getProductIds());
                    }
                }
            }

            Set<Product> removedProducts = new HashSet<>();
            if (existingWarehouse.getProducts() != null) {
                for (Product p : existingWarehouse.getProducts()) {
                    if (!newProductIds.contains(p.getId())) {
                        removedProducts.add(p);
                    }
                }
            }

            // For each removed product, decrease the quantities of all its variants,
            // and delete the corresponding InventoryItem records in this warehouse!
            for (Product p : removedProducts) {
                if (p.getVariants() != null) {
                    for (Variant variant : p.getVariants()) {
                        List<InventoryItem> itemsToDelete = inventoryItemRepository.findByVariantIdAndWarehouseId(variant.getId(), id);
                        for (InventoryItem item : itemsToDelete) {
                            if (item.getQuantity() != null && item.getQuantity() > 0) {
                                int currentQty = variant.getQuantity() != null ? variant.getQuantity() : 0;
                                variant.setQuantity(Math.max(0, currentQty - item.getQuantity()));
                            }
                            if (variant.getInventoryItems() != null) {
                                variant.getInventoryItems().remove(item);
                            }
                            variantRepository.save(variant);

                            StorageLocation loc = item.getStorageLocation();
                            if (loc != null && loc.getInventoryItems() != null) {
                                loc.getInventoryItems().remove(item);
                            }

                            inventoryItemRepository.delete(item);
                        }
                    }
                }
            }
        }

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

        // 2. Automatically link existing matching products
        if (warehouse.getCategories() == null || warehouse.getCategories().isEmpty()) {
            List<Product> allProducts = productRepository.findAll();
            if (warehouse.getProducts() == null) {
                warehouse.setProducts(new HashSet<>());
            }
            warehouse.getProducts().addAll(allProducts);
            warehouseRepository.save(warehouse);
        } else {
            List<Product> allProducts = productRepository.findAll();
            for (Product p : allProducts) {
                boolean categoryAllowed = false;
                if (p.getCategories() != null) {
                    for (com.hnh.entity.product.Category productCat : p.getCategories()) {
                        com.hnh.entity.product.Category curr = productCat;
                        while (curr != null) {
                            if (warehouse.getCategories().contains(curr)) {
                                categoryAllowed = true;
                                break;
                            }
                            curr = curr.getParentCategory();
                        }
                        if (categoryAllowed) break;
                    }
                }
                if (categoryAllowed) {
                    if (warehouse.getProducts() == null) {
                        warehouse.setProducts(new HashSet<>());
                    }
                    warehouse.getProducts().add(p);
                }
            }
            warehouseRepository.save(warehouse);
        }

        // 3. Initialize InventoryItems for all Variants of assigned Products
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
        Warehouse warehouse = warehouseRepository.findById(id).orElse(null);
        if (warehouse != null) {
            decreaseVariantQuantities(warehouse);
            warehouseRepository.delete(warehouse);
        }
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            delete(id);
        }
    }

    private void decreaseVariantQuantities(Warehouse warehouse) {
        if (warehouse.getLocations() != null) {
            for (StorageLocation location : warehouse.getLocations()) {
                if (location.getInventoryItems() != null) {
                    for (InventoryItem item : location.getInventoryItems()) {
                        if (item.getVariant() != null) {
                            Variant variant = item.getVariant();
                            if (item.getQuantity() != null && item.getQuantity() > 0) {
                                int currentQty = variant.getQuantity() != null ? variant.getQuantity() : 0;
                                variant.setQuantity(Math.max(0, currentQty - item.getQuantity()));
                            }
                            if (variant.getInventoryItems() != null) {
                                variant.getInventoryItems().remove(item);
                            }
                            variantRepository.save(variant);
                        }
                    }
                }
            }
        }
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
