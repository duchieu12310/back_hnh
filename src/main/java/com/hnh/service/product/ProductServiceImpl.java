package com.hnh.service.product;

import com.hnh.constant.ResourceName;
import com.hnh.constant.SearchFields;
import com.hnh.dto.product.ProductRequest;
import com.hnh.dto.product.ProductResponse;
import com.hnh.entity.product.Product;
import com.hnh.exception.ConflictException;
import com.hnh.mapper.product.ProductMapper;
import com.hnh.repository.product.ProductRepository;
import com.hnh.service.GenericService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl extends GenericService<Product, ProductRequest, ProductResponse> implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @PostConstruct
    public void initFields() {
        this.init(productRepository, productMapper, SearchFields.PRODUCT, ResourceName.PRODUCT);
    }

    @Override
    public ProductResponse save(ProductRequest request) {
        validateUniqueness(request, null);
        return super.save(request);
    }

    @Override
    public ProductResponse save(Long id, ProductRequest request) {
        validateUniqueness(request, id);
        return super.save(id, request);
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
