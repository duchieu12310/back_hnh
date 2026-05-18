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

import org.springframework.data.jpa.domain.Specification;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import com.hnh.entity.product.Brand;
import com.hnh.entity.product.Variant;
import java.util.ArrayList;

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

    @GetMapping("/shop")
    public ResponseEntity<ListResponse<ClientListedProductResponse>> getShopProducts(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "sort", required = false) @Nullable String sort,
            @RequestParam(name = "saleable", required = false) boolean saleable,
            @RequestParam(name = "search", required = false) @Nullable String search
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);

        Specification<Product> spec = (root, query, cb) -> {
            List<Predicate> wheres = new ArrayList<>();
            List<javax.persistence.criteria.Order> orders = new ArrayList<>();

            Join<Product, Variant> variant = root.join("variants", javax.persistence.criteria.JoinType.LEFT);

            if (saleable) {
                Subquery<Integer> subquery = query.subquery(Integer.class);
                Root<Variant> variantSq = subquery.from(Variant.class);
                subquery.select(cb.sum(variantSq.get("quantity")));
                subquery.where(cb.equal(variantSq.get("product"), root));
                subquery.groupBy(variantSq.get("product"));
                wheres.add(cb.greaterThan(cb.coalesce(subquery, 0), 0));
            }

            if (search != null && !search.isBlank()) {
                String likeQuery = "%" + search.trim().toLowerCase() + "%";
                Join<Product, Category> categories = root.join("categories", javax.persistence.criteria.JoinType.LEFT);
                Join<Product, Brand> brand = root.join("brand", javax.persistence.criteria.JoinType.LEFT);
                wheres.add(cb.or(
                    cb.like(cb.lower(root.get("name")), likeQuery),
                    cb.like(cb.lower(categories.get("name")), likeQuery),
                    cb.like(cb.lower(brand.get("name")), likeQuery)
                ));
            }

            if ("lowest-price".equals(sort)) {
                orders.add(cb.asc(cb.min(variant.get("price"))));
            }

            if ("highest-price".equals(sort)) {
                orders.add(cb.desc(cb.max(variant.get("price"))));
            }

            if ("random".equals(sort)) {
                orders.add(cb.asc(cb.function("RAND", Void.class)));
            }

            query.where(wheres.toArray(Predicate[]::new));
            query.groupBy(root.get("id"));
            query.orderBy(orders);

            return query.getRestriction();
        };

        Page<Product> products = productRepository.findAll(spec, pageable);

        List<Long> productIds = products.map(Product::getId).toList();
        List<SimpleProductInventory> productInventories = projectionRepository.findSimpleProductInventories(productIds);

        List<ClientListedProductResponse> clientListedProductResponses = products
                .map(product -> clientProductMapper.entityToListedResponse(product, productInventories)).toList();

        return ResponseEntity.status(HttpStatus.OK).body(ListResponse.of(clientListedProductResponses, products));
    }

    @GetMapping
    public ResponseEntity<ListResponse<ClientListedProductResponse>> getAllProducts(
            @RequestParam(name = "page", defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int page,
            @RequestParam(name = "size", defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int size,
            @RequestParam(name = "filter", required = false) @Nullable String filter,
            @RequestParam(name = "sort", required = false) @Nullable String sort,
            @RequestParam(name = "search", required = false) @Nullable String search,
            @RequestParam(name = "saleable", required = false) boolean saleable,
            @RequestParam(name = "newable", required = false) boolean newable,
            @RequestParam(name = "slowSelling", required = false) boolean slowSelling,
            @RequestParam(name = "topSelling", required = false) boolean topSelling
    ) {
        // Phân trang
        Pageable pageable = PageRequest.of(page - 1, size);

        // Lấy danh sách sản phẩm theo điều kiện lọc và phân trang
        Page<Product> products = productRepository.findByParams(filter, sort, search, saleable, newable, slowSelling || topSelling, pageable);

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
            @RequestParam(name = "slowSelling", required = false) boolean slowSelling,
            @RequestParam(name = "topSelling", required = false) boolean topSelling
    ) {
        // Tìm danh mục theo slug, ném lỗi 404 nếu không tồn tại
        Category category = categoryRepository.findBySlug(slug)
                .orElseThrow(() -> new ResourceNotFoundException(ResourceName.CATEGORY, FieldName.SLUG, slug));

        // Dùng WITH RECURSIVE query để lấy ID của danh mục cha + tất cả danh mục con (mọi cấp)
        List<Long> categoryIds = categoryRepository.findAllDescendantIds(category.getId());

        // Xây dựng filter theo danh sách category IDs
        String categoryFilter = "categories.id=in=(" + categoryIds.stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining(",")) + ")";
        String finalFilter = (filter == null || filter.isBlank()) ? categoryFilter : categoryFilter + ";" + filter;

        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Product> products = productRepository.findByParams(finalFilter, sort, search, saleable, newable, slowSelling || topSelling, pageable);

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

        Page<Product> relatedProducts = productRepository.findByParams(
                String.format("categories.id==%s;id!=%s",
                        product.getCategories().stream()
                                .findFirst()
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

