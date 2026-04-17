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

                    if (p.getCategories() == null || p.getCategories().isEmpty()) return false;

                    // Check if any associated category matches the requested filters
                    return p.getCategories().stream().anyMatch(cat -> {
                        Category l3 = cat;
                        if (request.getCategoryL3Ids() != null && !request.getCategoryL3Ids().isEmpty() && !request.getCategoryL3Ids().contains(l3.getId())) {
                            return false;
                        }

                        Category l2 = l3.getParentCategory();
                        if (request.getCategoryL2Ids() != null && !request.getCategoryL2Ids().isEmpty() && (l2 == null || !request.getCategoryL2Ids().contains(l2.getId()))) {
                            return false;
                        }

                        Category l1 = (l2 != null) ? l2.getParentCategory() : null;
                        if (request.getCategoryL1Ids() != null && !request.getCategoryL1Ids().isEmpty() && (l1 == null || !request.getCategoryL1Ids().contains(l1.getId()))) {
                            return false;
                        }

                        return true;
                    });
                })
                .collect(Collectors.toSet());

        // 3. Group by Hierarchy: L1 -> L2 -> L3 -> Product+Location -> Variant
        Map<Long, CategoryLevel1Node> l1Nodes = new HashMap<>();
        Map<Long, CategoryLevel2Node> l2Nodes = new HashMap<>();
        Map<Long, CategoryLevel3Node> l3Nodes = new HashMap<>();

        for (Product product : productsToInclude) {
            for (Category leafCat : product.getCategories()) {
                // Determine full ancestry path
                List<Category> path = new ArrayList<>();
                Category curr = leafCat;
                while (curr != null) {
                    path.add(0, curr);
                    curr = curr.getParentCategory();
                }

                Category l1Cat = path.size() >= 1 ? path.get(0) : null;
                Category l2Cat = path.size() >= 2 ? path.get(1) : null;
                Category l3Cat = path.size() >= 3 ? path.get(2) : null;

                // Filters (Apply to the specific category path of this product)
                if (request.getCategoryL3Ids() != null && !request.getCategoryL3Ids().isEmpty() && (l3Cat == null || !request.getCategoryL3Ids().contains(l3Cat.getId()))) continue;
                if (request.getCategoryL2Ids() != null && !request.getCategoryL2Ids().isEmpty() && (l2Cat == null || !request.getCategoryL2Ids().contains(l2Cat.getId()))) continue;
                if (request.getCategoryL1Ids() != null && !request.getCategoryL1Ids().isEmpty() && (l1Cat == null || !request.getCategoryL1Ids().contains(l1Cat.getId()))) continue;

                // 1. Prepare Product Storage Responses for this product
                List<ProductStorageResponse> pResponses = new ArrayList<>();
                Map<Long, List<InventoryItem>> itemsByLocation = new HashMap<>();
                if (warehouse != null) {
                    for (Variant variant : product.getVariants()) {
                        List<InventoryItem> varItems = itemsByVariantId.getOrDefault(variant.getId(), Collections.emptyList());
                        for (InventoryItem item : varItems) {
                            itemsByLocation.computeIfAbsent(item.getStorageLocation().getId(), id -> new ArrayList<>()).add(item);
                        }
                    }
                }

                if (!itemsByLocation.isEmpty()) {
                    for (Map.Entry<Long, List<InventoryItem>> entry : itemsByLocation.entrySet()) {
                        StorageLocation loc = entry.getValue().get(0).getStorageLocation();
                        pResponses.add(mapToProductStorageResponse(product, loc, entry.getValue()));
                    }
                } else if (warehouse != null) {
                    if (request.getAisle() == null && request.getShelf() == null && request.getBin() == null) {
                        StorageLocation defaultLoc = warehouse.getLocations().isEmpty() ? null : warehouse.getLocations().get(0);
                        if (defaultLoc != null) pResponses.add(mapToProductStorageResponse(product, defaultLoc, Collections.emptyList()));
                    }
                } else {
                    pResponses.add(mapToProductStorageResponse(product, null, Collections.emptyList()));
                }

                // 2. Attach to Tree at correct level
                if (leafCat.getLevel() == 1) {
                    CategoryLevel1Node node1 = l1Nodes.computeIfAbsent(leafCat.getId(), id -> new CategoryLevel1Node().setId(leafCat.getId()).setName(leafCat.getName()).setChildren(new ArrayList<>()).setProducts(new ArrayList<>()));
                    if (node1.getProducts() == null) node1.setProducts(new ArrayList<>());
                    node1.getProducts().addAll(pResponses);
                } else if (leafCat.getLevel() == 2) {
                    CategoryLevel1Node node1 = l1Nodes.computeIfAbsent(l1Cat.getId(), id -> new CategoryLevel1Node().setId(l1Cat.getId()).setName(l1Cat.getName()).setChildren(new ArrayList<>()).setProducts(new ArrayList<>()));
                    CategoryLevel2Node node2 = l2Nodes.computeIfAbsent(leafCat.getId(), id -> new CategoryLevel2Node().setId(leafCat.getId()).setName(leafCat.getName()).setChildren(new ArrayList<>()).setProducts(new ArrayList<>()));
                    if (node1.getChildren().stream().noneMatch(c -> c.getId().equals(node2.getId()))) node1.getChildren().add(node2);
                    if (node2.getProducts() == null) node2.setProducts(new ArrayList<>());
                    node2.getProducts().addAll(pResponses);
                } else {
                    // Level 3 (or deeper, capped at 3 DTOs)
                    CategoryLevel1Node node1 = l1Nodes.computeIfAbsent(l1Cat.getId(), id -> new CategoryLevel1Node().setId(l1Cat.getId()).setName(l1Cat.getName()).setChildren(new ArrayList<>()).setProducts(new ArrayList<>()));
                    CategoryLevel2Node node2 = l2Nodes.computeIfAbsent(l2Cat.getId(), id -> new CategoryLevel2Node().setId(l2Cat.getId()).setName(l2Cat.getName()).setChildren(new ArrayList<>()).setProducts(new ArrayList<>()));
                    CategoryLevel3Node node3 = l3Nodes.computeIfAbsent(leafCat.getId(), id -> new CategoryLevel3Node().setId(leafCat.getId()).setName(leafCat.getName()).setProducts(new ArrayList<>()));
                    
                    if (node1.getChildren().stream().noneMatch(c -> c.getId().equals(node2.getId()))) node1.getChildren().add(node2);
                    if (node2.getChildren().stream().noneMatch(c -> c.getId().equals(node3.getId()))) node2.getChildren().add(node3);
                    node3.getProducts().addAll(pResponses);
                }
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

    @Override
    public Warehouse deductFromClosestWarehouse(com.hnh.entity.order.Order order) {
        // 1. Tìm tất cả các kho có liên quan đến các sản phẩm trong đơn hàng
        List<Warehouse> allWarehouses = warehouseRepository.findAll();

        // 2. Lọc ra những kho có ĐỦ tất cả các mặt hàng trong đơn hàng
        List<Warehouse> capableWarehouses = allWarehouses.stream()
                .filter(w -> canFulfillOrder(w, order))
                .collect(Collectors.toList());

        if (capableWarehouses.isEmpty()) {
            throw new RuntimeException("Không tìm thấy kho nào có đủ tất cả sản phẩm trong đơn hàng này.");
        }

        // 3. Chọn kho gần nhất
        Warehouse closestWarehouse = capableWarehouses.stream()
                .min(Comparator.comparingInt(w -> calculateProximityScore(w, order)))
                .orElse(capableWarehouses.get(0));

        // 4. Thực hiện trừ kho tại kho đã chọn
        for (com.hnh.entity.order.OrderVariant ov : order.getOrderVariants()) {
            List<InventoryItem> itemsInWarehouse = inventoryItemRepository.findByVariantIdAndWarehouseId(ov.getVariant().getId(), closestWarehouse.getId());
            
            if (itemsInWarehouse.isEmpty()) {
                throw new RuntimeException("Lỗi logic: Không tìm thấy vật phẩm trong kho đã chọn");
            }

            // Để đơn giản, trừ tại vị trí đầu tiên tìm thấy trong kho này
            // (Trong thực tế có thể chia nhỏ nếu vị trí 1 không đủ, nhưng logic check canFulfillOrder 
            // đảm bảo tổng kho có đủ)
            InventoryItem item = itemsInWarehouse.get(0);
            item.setQuantity(item.getQuantity() - ov.getQuantity());
            inventoryItemRepository.save(item);

            // Cập nhật tổng số lượng biến thể
            Variant v = ov.getVariant();
            v.setQuantity((v.getQuantity() != null ? v.getQuantity() : 0) - ov.getQuantity());
            variantRepository.save(v);
        }

        return closestWarehouse;
    }

    private boolean canFulfillOrder(Warehouse warehouse, com.hnh.entity.order.Order order) {
        for (com.hnh.entity.order.OrderVariant ov : order.getOrderVariants()) {
            int totalStockInWarehouse = inventoryItemRepository.findByVariantIdAndWarehouseId(ov.getVariant().getId(), warehouse.getId())
                    .stream()
                    .mapToInt(ii -> ii.getQuantity() != null ? ii.getQuantity() : 0)
                    .sum();
            if (totalStockInWarehouse < ov.getQuantity()) return false;
        }
        return true;
    }

    private int calculateProximityScore(Warehouse warehouse, com.hnh.entity.order.Order order) {
        if (warehouse.getAddress() == null) return 100;

        String wProvince = warehouse.getAddress().getProvince().getName();
        String wDistrict = warehouse.getAddress().getDistrict().getName();

        if (wProvince.equalsIgnoreCase(order.getToProvinceName())) {
            if (wDistrict.equalsIgnoreCase(order.getToDistrictName())) {
                return 0; // Ưu tiên nhất: Cùng Quận/Huyện
            }
            return 10; // Ưu tiên nhì: Cùng Tỉnh
        }
        return 100; // Tỉnh khác
    }

    @Override
    public void restoreStockToWarehouse(com.hnh.entity.order.Order order, Warehouse warehouse) {
        for (com.hnh.entity.order.OrderVariant ov : order.getOrderVariants()) {
            List<InventoryItem> itemsInWarehouse = inventoryItemRepository.findByVariantIdAndWarehouseId(ov.getVariant().getId(), warehouse.getId());
            
            InventoryItem item;
            if (itemsInWarehouse.isEmpty()) {
                // Nếu item không còn tồn tại trong kho (hy hữu), tạo mới tại vị trí đầu tiên của kho
                StorageLocation firstLoc = warehouse.getLocations().isEmpty() ? null : warehouse.getLocations().get(0);
                item = new InventoryItem().setVariant(ov.getVariant()).setStorageLocation(firstLoc).setQuantity(0);
            } else {
                item = itemsInWarehouse.get(0);
            }

            item.setQuantity((item.getQuantity() != null ? item.getQuantity() : 0) + ov.getQuantity());
            inventoryItemRepository.save(item);

            // Hoàn lại tổng số lượng biến thể
            Variant v = ov.getVariant();
            v.setQuantity((v.getQuantity() != null ? v.getQuantity() : 0) + ov.getQuantity());
            variantRepository.save(v);
        }
    }
}

