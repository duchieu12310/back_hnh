package com.hnh.mapper.promotion;

import com.hnh.dto.client.ClientPromotionResponse;
import com.hnh.dto.promotion.PromotionRequest;
import com.hnh.dto.promotion.PromotionResponse;
import com.hnh.entity.product.Category;
import com.hnh.entity.product.Product;
import com.hnh.entity.promotion.Promotion;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.product.ProductMapper;
import com.hnh.repository.product.CategoryRepository;
import com.hnh.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {MapperUtils.class, ProductMapper.class})
public abstract class PromotionMapper implements GenericMapper<Promotion, PromotionRequest, PromotionResponse> {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    @Mapping(source = "productIds", target = "products", qualifiedByName = "mapToProducts")
    public abstract Promotion requestToEntity(PromotionRequest request);

    @Override
    @Mapping(source = "productIds", target = "products", qualifiedByName = "mapToProducts")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract Promotion partialUpdate(@MappingTarget Promotion entity, PromotionRequest request);

    @AfterMapping
    protected void addProductsFromCategories(@MappingTarget Promotion promotion, PromotionRequest request) {
        if (request.getCategoryIds() != null && !request.getCategoryIds().isEmpty()) {
            Set<Product> productsFromCategories = new HashSet<>();
            for (Long categoryId : request.getCategoryIds()) {
                Category category = categoryRepository.getById(categoryId);
                collectProductsRecursively(category, productsFromCategories);
            }

            if (promotion.getProducts() == null) {
                promotion.setProducts(new HashSet<>());
            }
            promotion.getProducts().addAll(productsFromCategories);
        }
    }

    private void collectProductsRecursively(Category category, Set<Product> targetSet) {
        if (category == null) {
            return;
        }
        if (category.getProducts() != null) {
            targetSet.addAll(category.getProducts());
        }
        if (category.getChildren() != null) {
            for (Category child : category.getChildren()) {
                collectProductsRecursively(child, targetSet);
            }
        }
    }

    @Mapping(source = "id", target = "promotionId")
    @Mapping(source = "percent", target = "promotionPercent")
    @Mapping(source = "name", target = "promotionName")
    public abstract ClientPromotionResponse entityToClientResponse(Promotion promotion);

}

