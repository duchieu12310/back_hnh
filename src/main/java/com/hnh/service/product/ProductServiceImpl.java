package com.hnh.service.product;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.dto.product.VariantRequest;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Variant;
import com.hnh.entity.warehouse.Warehouse;
import com.hnh.entity.warehouse.StorageLocation;
import com.hnh.entity.warehouse.InventoryItem;
import com.hnh.exception.ConflictException;
import com.hnh.mapper.product.ProductMapper;
import com.hnh.repository.product.ProductRepository;
import com.hnh.repository.warehouse.WarehouseRepository;
import com.hnh.repository.warehouse.StorageLocationRepository;
import com.hnh.repository.warehouse.InventoryItemRepository;
import com.hnh.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl extends GenericService<Product, ProductRequest, ProductResponse> implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final WarehouseRepository warehouseRepository;
    private final StorageLocationRepository storageLocationRepository;
    private final InventoryItemRepository inventoryItemRepository;

    @PostConstruct
    public void initFields() {
        this.init(productRepository, productMapper, SearchFields.PRODUCT, ResourceName.PRODUCT);
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        validateUniqueness(request, null);
        ProductResponse response = super.save(request);
        initializeInventoryForProduct(response.getId());
        return response;
    }

    @Override
    public ProductResponse save(Long id, ProductRequest request) {
        validateUniqueness(request, id);

        // Detect and clean up deleted variants' inventory items
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct != null && existingProduct.getVariants() != null && request.getVariants() != null) {
            java.util.Set<Long> requestVariantIds = request.getVariants().stream()
                    .map(VariantRequest::getId)
                    .filter(java.util.Objects::nonNull)
                    .collect(Collectors.toSet());

            for (Variant variant : existingProduct.getVariants()) {
                if (!requestVariantIds.contains(variant.getId())) {
                    List<InventoryItem> itemsToDelete = inventoryItemRepository.findByVariantId(variant.getId());
                    if (itemsToDelete != null && !itemsToDelete.isEmpty()) {
                        inventoryItemRepository.deleteAll(itemsToDelete);
                    }
                }
            }
        }

        ProductResponse response = super.save(id, request);
        initializeInventoryForProduct(response.getId());
        return response;
    }

    private void initializeInventoryForProduct(Long productId) {
        Product product = productRepository.findById(productId).orElse(null);
        if (product == null) return;

        List<Warehouse> warehouses = warehouseRepository.findAll();
        for (Warehouse warehouse : warehouses) {
            boolean shouldInclude = false;
            if (warehouse.getCategories() != null && !warehouse.getCategories().isEmpty()) {
                if (warehouse.getProducts() != null) {
                    shouldInclude = warehouse.getProducts().stream().anyMatch(p -> p.getId().equals(productId));
                }
            } else {
                shouldInclude = true;
                if (warehouse.getProducts() == null) {
                    warehouse.setProducts(new HashSet<>());
                }
                if (warehouse.getProducts().stream().noneMatch(p -> p.getId().equals(productId))) {
                    warehouse.getProducts().add(product);
                    warehouseRepository.save(warehouse);
                }
            }

            if (shouldInclude) {
                StorageLocation defaultLocation = storageLocationRepository
                        .findByWarehouseIdAndAisleAndShelfAndBin(warehouse.getId(), null, null, null)
                        .orElseGet(() -> storageLocationRepository.save(
                                new StorageLocation()
                                        .setWarehouse(warehouse)
                                        .setAisle(null)
                                        .setShelf(null)
                                        .setBin(null)
                        ));

                if (product.getVariants() != null) {
                    for (Variant variant : product.getVariants()) {
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

    private void validateUniqueness(ProductRequest request, Long id) {
        String code = request.getCode();
        String slug = request.getSlug();

        if (id == null) {
            if (code != null && productRepository.existsByCode(code)) {
                throw new ConflictException("Mã sản phẩm đã tồn tại");
            }
            if (slug != null && productRepository.existsBySlug(slug)) {
                throw new ConflictException("Slug sản phẩm đã tồn tại");
            }
        } else {
            if (code != null && productRepository.existsByCodeAndIdNot(code, id)) {
                throw new ConflictException("Mã sản phẩm đã tồn tại");
            }
            if (slug != null && productRepository.existsBySlugAndIdNot(slug, id)) {
                throw new ConflictException("Slug sản phẩm đã tồn tại");
            }
        }
    }
}
