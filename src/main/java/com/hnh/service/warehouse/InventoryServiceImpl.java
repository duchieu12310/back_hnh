package com.hnh.service.warehouse;

import com.hnh.dto.warehouse.*;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Variant;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.entity.warehouse.Warehouse;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.repository.product.VariantRepository;
import com.hnh.entity.warehouse.InventoryItem;
import com.hnh.repository.product.ProductRepository;
import com.hnh.repository.warehouse.InventoryItemRepository;
import com.hnh.repository.warehouse.StorageLocationRepository;
import com.hnh.repository.warehouse.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final VariantRepository variantRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final CategoryRepository categoryRepository;
    private final InventoryItemRepository inventoryItemRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;


    @Override
    public List<CategoryLevel1Node> getHierarchicalInventory(InventoryFilterRequest request) {
        Set<Product> productsToInclude;
        Map<Long, List<InventoryItem>> itemsByVariantId;
        Warehouse warehouse = null;

        if (request.getWarehouseId() != null) {
            warehouse = warehouseRepository.findById(request.getWarehouseId())
                    .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException("Warehouse", "id", request.getWarehouseId()));

            // 1. Get all InventoryItems for this warehouse (optionally filtered by coordinates)
            final Long finalWarehouseId = request.getWarehouseId();
            Specification<InventoryItem> itemSpec = (root, query, cb) -> {
                List<Predicate> predicates = new ArrayList<>();
                Join<InventoryItem, StorageLocation> locationJoin = root.join("storageLocation", JoinType.INNER);
                predicates.add(cb.equal(locationJoin.get("warehouse").get("id"), finalWarehouseId));

                if (request.getAisle() != null && !request.getAisle().isEmpty()) predicates.add(cb.equal(locationJoin.get("aisle"), request.getAisle()));
                if (request.getShelf() != null && !request.getShelf().isEmpty()) predicates.add(cb.equal(locationJoin.get("shelf"), request.getShelf()));
                if (request.getBin() != null && !request.getBin().isEmpty()) predicates.add(cb.equal(locationJoin.get("bin"), request.getBin()));

                // If specialized search for "Empty/Unassigned" items is requested (where coords are null)
                // In our system, these are represented by null in the database.
                
                return cb.and(predicates.toArray(new Predicate[0]));
            };
            List<InventoryItem> allItems = inventoryItemRepository.findAll(itemSpec);
            itemsByVariantId = allItems.stream().collect(Collectors.groupingBy(i -> i.getVariant().getId()));

            // 2. Identify Products assigned to this warehouse
            productsToInclude = new HashSet<>(warehouse.getProducts());
        } else {
            // Global Catalog Mode: fetch all products
            productsToInclude = new HashSet<>(productRepository.findAll());
            itemsByVariantId = Collections.emptyMap();
        }

        // Apply shared filters (Categories/IDs)
        productsToInclude = productsToInclude.stream()
                .filter(p -> {
                    if (request.getProductIds() != null && !request.getProductIds().isEmpty() && !request.getProductIds().contains(p.getId())) return false;
                    if (p.getCategory() == null) return false;

                    Category l3 = p.getCategory();
                    if (request.getCategoryL3Ids() != null && !request.getCategoryL3Ids().isEmpty() && !request.getCategoryL3Ids().contains(l3.getId())) return false;

                    Category l2 = l3.getParentCategory();
                    if (request.getCategoryL2Ids() != null && !request.getCategoryL2Ids().isEmpty() && (l2 == null || !request.getCategoryL2Ids().contains(l2.getId()))) return false;

                    Category l1 = (l2 != null) ? l2.getParentCategory() : null;
                    if (request.getCategoryL1Ids() != null && !request.getCategoryL1Ids().isEmpty() && (l1 == null || !request.getCategoryL1Ids().contains(l1.getId()))) return false;

                    return true;
                })
                .collect(Collectors.toSet());

        // 3. Group by Hierarchy: L1 -> L2 -> L3 -> Product+Location -> Variant
        Map<Long, CategoryLevel1Node> l1Nodes = new HashMap<>();
        Map<Long, CategoryLevel2Node> l2Nodes = new HashMap<>();
        Map<Long, CategoryLevel3Node> l3Nodes = new HashMap<>();

        for (Product product : productsToInclude) {
            Category l3 = product.getCategory();
            Category l2 = l3.getParentCategory();
            Category l1 = (l2 != null) ? l2.getParentCategory() : null;

            // Ensure L3 node exists
            CategoryLevel3Node node3 = l3Nodes.computeIfAbsent(l3.getId(), id -> {
                CategoryLevel3Node n = new CategoryLevel3Node();
                n.setId(l3.getId());
                n.setName(l3.getName());
                n.setProducts(new ArrayList<>());
                return n;
            });

            // Group variants of this product by their storage location within this warehouse
            Map<Long, List<InventoryItem>> itemsByLocation = new HashMap<>();
            if (warehouse != null) {
                for (Variant variant : product.getVariants()) {
                    List<InventoryItem> varItems = itemsByVariantId.getOrDefault(variant.getId(), Collections.emptyList());
                    for (InventoryItem item : varItems) {
                        itemsByLocation.computeIfAbsent(item.getStorageLocation().getId(), id -> new ArrayList<>()).add(item);
                    }
                }
            }

            // For each location where the product exists in this warehouse
            if (!itemsByLocation.isEmpty()) {
                for (Map.Entry<Long, List<InventoryItem>> entry : itemsByLocation.entrySet()) {
                    StorageLocation loc = entry.getValue().get(0).getStorageLocation();
                    node3.getProducts().add(mapToProductStorageResponse(product, loc, entry.getValue()));
                }
            } else if (warehouse != null) {
                // If no inventory records exist for THIS location filter but it's assigned to warehouse, 
                // we should show it in the default/first location if no coordinate filtering is active
                if (request.getAisle() == null && request.getShelf() == null && request.getBin() == null) {
                    StorageLocation defaultLoc = warehouse.getLocations().isEmpty() ? null : warehouse.getLocations().get(0);
                    if (defaultLoc != null) {
                        node3.getProducts().add(mapToProductStorageResponse(product, defaultLoc, Collections.emptyList()));
                    }
                }
            } else {
                // Global Catalog Mode: No warehouse/location context, return product with 0 quantities
                node3.getProducts().add(mapToProductStorageResponse(product, null, Collections.emptyList()));
            }

            // Build hierarchy upward
            if (l2 != null) {
                CategoryLevel2Node node2 = l2Nodes.computeIfAbsent(l2.getId(), id -> {
                    CategoryLevel2Node n = new CategoryLevel2Node();
                    n.setId(l2.getId());
                    n.setName(l2.getName());
                    n.setChildren(new ArrayList<>());
                    return n;
                });
                if (node2.getChildren().stream().noneMatch(c -> c.getId().equals(node3.getId()))) node2.getChildren().add(node3);

                if (l1 != null) {
                    CategoryLevel1Node node1 = l1Nodes.computeIfAbsent(l1.getId(), id -> {
                        CategoryLevel1Node n = new CategoryLevel1Node();
                        n.setId(l1.getId());
                        n.setName(l1.getName());
                        n.setChildren(new ArrayList<>());
                        return n;
                    });
                    if (node1.getChildren().stream().noneMatch(c -> c.getId().equals(node2.getId()))) node1.getChildren().add(node2);
                } else {
                    // L2 is actually Top Level (Unlikely in a 3-level system but for safety)
                    l1Nodes.computeIfAbsent(l2.getId(), id -> new CategoryLevel1Node().setId(l2.getId()).setName(l2.getName()).setChildren(new ArrayList<>()));
                }
            } else {
                // L3 is Top Level
                l1Nodes.computeIfAbsent(l3.getId(), id -> new CategoryLevel1Node().setId(l3.getId()).setName(l3.getName()).setChildren(new ArrayList<>()));
            }
        }

        return new ArrayList<>(l1Nodes.values());
    }

    private ProductStorageResponse mapToProductStorageResponse(Product product, StorageLocation loc, List<InventoryItem> items) {
        ProductStorageResponse pResponse = new ProductStorageResponse();
        pResponse.setProductId(product.getId());
        pResponse.setProductName(product.getName());
        pResponse.setProductCode(product.getCode());
        pResponse.setStorageLocationId(loc != null ? loc.getId() : null);
        pResponse.setAisle(loc != null ? loc.getAisle() : "");
        pResponse.setShelf(loc != null ? loc.getShelf() : "");
        pResponse.setBin(loc != null ? loc.getBin() : "");

        Map<Long, InventoryItem> itemMap = items.stream().collect(Collectors.toMap(i -> i.getVariant().getId(), i -> i));

        pResponse.setVariants(product.getVariants().stream().map(v -> {
            InventoryItem item = itemMap.get(v.getId());
            VariantInventoryDto vDto = new VariantInventoryDto();
            vDto.setVariantId(v.getId());
            vDto.setSku(v.getSku());
            vDto.setProperties(v.getProperties() != null ? v.getProperties().toString() : "");
            vDto.setQuantityInLocation(item != null ? item.getQuantity() : 0);
            vDto.setTotalVariantQuantity(v.getQuantity() != null ? v.getQuantity() : 0);
            return vDto;
        }).collect(Collectors.toList()));

        return pResponse;
    }

    @Override
    public void updateInventory(AutoSaveInventoryRequest dto) {
        Variant variant = variantRepository.findById(dto.getVariantId())
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException("Variant", "id", dto.getVariantId()));

        StorageLocation location = storageLocationRepository.findById(dto.getStorageLocationId())
                .orElseThrow(() -> new com.hnh.exception.ResourceNotFoundException("StorageLocation", "id", dto.getStorageLocationId()));

        InventoryItem item = inventoryItemRepository.findByVariantIdAndStorageLocationId(variant.getId(), location.getId())
                .orElse(new InventoryItem().setVariant(variant).setStorageLocation(location).setQuantity(0));

        if (dto.getNewQuantity() != null) {
            int oldItemQuantity = item.getQuantity() != null ? item.getQuantity() : 0;
            int delta = dto.getNewQuantity() - oldItemQuantity;

            // 1. Cập nhật số lượng tại vị trí cụ thể (InventoryItem)
            item.setQuantity(dto.getNewQuantity());
            inventoryItemRepository.save(item);

            // 2. Cập nhật tổng số lượng của Biến thể (Variant) theo mức chênh lệch (Delta)
            int currentTotal = variant.getQuantity() != null ? variant.getQuantity() : 0;
            variant.setQuantity(currentTotal + delta);
            variantRepository.save(variant);

        }
    }
}

