package com.hnh.controller.client;

import com.hnh.constant.AppConstants;
import com.hnh.constant.SearchFields;
import com.hnh.dto.client.ClientBrandResponse;
import com.hnh.dto.client.GlobalSearchResponse;
import com.hnh.entity.product.Brand;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Product;
import com.hnh.mapper.client.ClientCategoryMapper;
import com.hnh.mapper.client.ClientProductMapper;
import com.hnh.projection.product.SimpleProductInventory;
import com.hnh.repository.ProjectionRepository;
import com.hnh.repository.product.BrandRepository;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.repository.product.ProductRepository;
import com.hnh.utils.SearchUtils;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/client-api/search")
@AllArgsConstructor
@CrossOrigin(AppConstants.FRONTEND_HOST)
public class ClientSearchController {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final ClientProductMapper clientProductMapper;
    private final ClientCategoryMapper clientCategoryMapper;
    private final ProjectionRepository projectionRepository;

    @GetMapping
    public ResponseEntity<GlobalSearchResponse> searchEverything(@RequestParam String query) {
        String likeQuery = "%" + query.trim().toLowerCase() + "%";

        // 1. Tìm kiếm Sách (Products) - Sử dụng Specification tùy chỉnh để ép LEFT JOIN
        org.springframework.data.jpa.domain.Specification<Product> productSpec = (root, criteriaQuery, cb) -> {
            criteriaQuery.distinct(true); // Tránh lặp sản phẩm khi có nhiều category
            
            javax.persistence.criteria.Join<Product, Category> categories = root.join("categories", javax.persistence.criteria.JoinType.LEFT);
            javax.persistence.criteria.Join<Product, Brand> brand = root.join("brand", javax.persistence.criteria.JoinType.LEFT);
            
            return cb.or(
                cb.like(cb.lower(root.get("name")), likeQuery),
                cb.like(cb.lower(categories.get("name")), likeQuery),
                cb.like(cb.lower(brand.get("name")), likeQuery)
            );
        };

        List<Product> products = productRepository.findAll(productSpec, PageRequest.of(0, 10)).getContent();
        
        List<Long> productIds = products.stream().map(Product::getId).toList();
        List<SimpleProductInventory> productInventories = projectionRepository.findSimpleProductInventories(productIds);

        // 2. Tìm kiếm Danh mục (Thể loại sách)
        org.springframework.data.jpa.domain.Specification<Category> catSpec = (root, cq, cb) -> 
            cb.like(cb.lower(root.get("name")), likeQuery);
        List<Category> categories = categoryRepository.findAll(catSpec, PageRequest.of(0, 5)).getContent();

        // 3. Tìm kiếm Tác giả / Thương hiệu (Brands)
        org.springframework.data.jpa.domain.Specification<Brand> brandSpec = (root, cq, cb) -> 
            cb.like(cb.lower(root.get("name")), likeQuery);
        List<Brand> brands = brandRepository.findAll(brandSpec, PageRequest.of(0, 5)).getContent();

        GlobalSearchResponse response = GlobalSearchResponse.builder()
                .products(products.stream()
                        .map(p -> clientProductMapper.entityToListedResponse(p, productInventories))
                        .collect(Collectors.toList()))
                .categories(clientCategoryMapper.entityToResponse(categories, 1))
                .brands(brands.stream()
                        .map(brand -> new ClientBrandResponse()
                                .setBrandId(brand.getId())
                                .setBrandName(brand.getName()))
                        .collect(Collectors.toList()))
                .build();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
