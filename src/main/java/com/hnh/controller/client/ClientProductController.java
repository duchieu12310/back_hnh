package com.hnh.controller.client;

import com.hnh.constant.AppConstants;
import com.hnh.constant.FieldName;
import com.hnh.constant.ResourceName;
import com.hnh.dto.ListResponse;
import com.hnh.dto.client.ClientListedProductResponse;
import com.hnh.dto.client.ClientProductResponse;
import com.hnh.entity.BaseEntity;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Product;
import com.hnh.exception.ResourceNotFoundException;
import com.hnh.mapper.client.ClientProductMapper;
import com.hnh.projection.product.SimpleProductInventory;
import com.hnh.repository.ProjectionRepository;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.repository.product.ProductRepository;
import com.hnh.repository.review.ReviewRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client-api/products")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ClientProductController {

    private ProductRepository productRepository;
    private CategoryRepository categoryRepository;
    private ProjectionRepository projectionRepository;
    private ClientProductMapper clientProductMapper;
    private ReviewRepository reviewRepository;

    @GetMapping
    public ResponseEntity<ListResponse<ClientListedProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "filter", required = false) @Nullable String filter,
            @RequestParam(name = "sort", required = false) @Nullable String sort,
            @RequestParam(name = "search", required = false) @Nullable String search,
            @RequestParam(name = "saleable", required = false) boolean saleable,
            @RequestParam(name = "newable", required = false) boolean newable,
            @RequestParam(name = "slowSelling", required = false) boolean slowSelling
    ) {
        // Phân trang
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách sản phẩm theo điều kiện lọc và phân trang
        Page<Product> products = productRepository.findByParams(filter, sort, search, saleable, newable, slowSelling, pageable);

        // Lấy thông tin tồn kho của sản phẩm
        List<Long> productIds = products.map(Product::getId).toList();
        List<SimpleProductInventory> productInventories = projectionRepository.findSimpleProductInventories(productIds);

        List<ClientListedProductResponse> clientListedProductResponses = products
                .map(product -> clientProductMapper.entityToListedResponse(product, productInventories)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(ListResponse.of(clientListedProductResponses, products));
    }

    @GetMapping("/category/{slug}")
    public ResponseEntity<ListResponse<ClientListedProductResponse>> getProductsByCategory(
            @PathVariable String slug,
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "filter", required = false) @Nullable String filter,
            @RequestParam(name = "sort", required = false) @Nullable String sort,
            @RequestParam(name = "search", required = false) @Nullable String search,
            @RequestParam(name = "saleable", required = false) boolean saleable,
            @RequestParam(name = "newable", required = false) boolean newable,
            @RequestParam(name = "slowSelling", required = false) boolean slowSelling
    ) {
        // Tìm danh mục theo slug, ném lỗi 404 nếu không tồn tại
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CATEGORY, FieldName.SLUG, slug));

        // Dùng WITH RECURSIVE query để lấy ID của danh mục cha + tất cả danh mục con (mọi cấp)
        List<Long> categoryIds = categoryRepository.findAllDescendantIds(category.getId());

        // Xây dựng filter theo danh sách category IDs
        String categoryFilter = "category.id=in=(" + categoryIds.stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining(",")) + ")";
        String finalFilter = (filter == null || filter.isBlank()) ? categoryFilter : categoryFilter + ";" + filter;

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> products = productRepository.findByParams(finalFilter, sort, search, saleable, newable, slowSelling, pageable);

        List<Long> productIds = products.map(Product::getId).toList();
        List<SimpleProductInventory> productInventories = projectionRepository.findSimpleProductInventories(productIds);

        List<ClientListedProductResponse> clientListedProductResponses = products
                .map(product -> clientProductMapper.entityToListedResponse(product, productInventories)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(ListResponse.of(clientListedProductResponses, products));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ClientProductResponse> getProduct(@PathVariable String slug) {
        Product product = productRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.PRODUCT, FieldName.SLUG, slug));

        List<SimpleProductInventory> productInventories = projectionRepository
                .findSimpleProductInventories(List.of(product.getId()));

        int averageRatingScore = reviewRepository.findAverageRatingScoreByProductId(product.getId());
        int countReviews = reviewRepository.countByProductId(product.getId());

        // Related Products
        Page<Product> relatedProducts = productRepository.findByParams(
                String.format("category.id==%s;id!=%s",
                        Optional.ofNullable(product.getCategory())
                                .map(BaseEntity::getId)
                                .map(Object::toString)
                                .orElse("0"),
                        product.getId()),
                "random",
                null,
                false,
                false,
                false,
                PageRequest.of(0, 4));

        List<Long> relatedProductIds = relatedProducts.map(Product::getId).toList();
        List<SimpleProductInventory> relatedProductInventories = projectionRepository
                .findSimpleProductInventories(relatedProductIds);

        List<ClientListedProductResponse> relatedProductResponses = relatedProducts
                .map(p -> clientProductMapper.entityToListedResponse(p, relatedProductInventories)).toList();

        // Result
        ClientProductResponse clientProductResponse = clientProductMapper
                .entityToResponse(product, productInventories, averageRatingScore, countReviews, relatedProductResponses);

        return ResponseEntity.status(HttpStatus.OK).body(clientProductResponse);
    }

}

