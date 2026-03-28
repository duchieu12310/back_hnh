package com.hnh.mapper.client;

import com.hnh.dto.client.ClientListedProductResponse;
import com.hnh.dto.client.ClientProductResponse;
import com.hnh.entity.general.Image;
import com.hnh.entity.product.Product;
import com.hnh.entity.product.Variant;
import com.hnh.mapper.general.ImageMapper;
import com.hnh.mapper.promotion.PromotionMapper;
import com.hnh.projection.inventory.SimpleProductInventory;
import com.hnh.repository.inventory.DocketVariantRepository;
import com.hnh.repository.promotion.PromotionRepository;
import com.hnh.utils.InventoryUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientProductMapper {

    private ImageMapper imageMapper;
    private ClientCategoryMapper clientCategoryMapper;
    private DocketVariantRepository docketVariantRepository;
    private PromotionRepository promotionRepository;
    private PromotionMapper promotionMapper;

    public ClientListedProductResponse entityToListedResponse(Product product, List<SimpleProductInventory> productInventories) {
        ClientListedProductResponse clientListedProductResponse = new ClientListedProductResponse();

        clientListedProductResponse
                .setProductId(product.getId())
                .setProductName(product.getName())
                .setProductSlug(product.getSlug())
                .setProductThumbnail(product.getImages().stream()
                        .filter(Image::getIsThumbnail)
                        .findAny()
                        .map(Image::getPath)
                        .orElse(null));

        List<Double> prices = product.getVariants().stream()
                .map(Variant::getPrice).distinct().sorted().collect(Collectors.toList());

        clientListedProductResponse.setProductPriceRange(
                prices.size() == 0
                        ? Collections.emptyList()
                        : prices.size() == 1
                        ? List.of(prices.get(0))
                        : List.of(prices.get(0), prices.get(prices.size() - 1))
        );

        clientListedProductResponse.setProductVariants(product.getVariants().stream()
                .map(variant -> {
                    Integer quantity = variant.getQuantity();
                    return new ClientListedProductResponse.ClientListedVariantResponse()
                            .setVariantId(variant.getId())
                            .setVariantPrice(variant.getPrice())
                            .setVariantCost(variant.getCost())
                            .setVariantSku(variant.getSku())
                            .setVariantProperties(variant.getProperties())
                            .setVariantImages(variant.getImages())
                            .setVariantStatus(variant.getStatus())
                            .setQuantity(quantity)
                            .setInventoryStatus(quantity != null && quantity > 0 ? "Còn hàng" : "Hết hàng");
                })
                .collect(Collectors.toList()));

        clientListedProductResponse.setProductSaleable(productInventories.stream()
                .filter(productInventory -> productInventory.getProductId().equals(product.getId()))
                .findAny()
                .map(productInventory -> productInventory.getCanBeSold() > 0)
                .orElse(false));

        clientListedProductResponse.setProductPromotion(promotionRepository
                .findActivePromotionByProductId(product.getId())
                .stream()
                .findFirst()
                .map(promotionMapper::entityToClientResponse)
                .orElse(null));

        return clientListedProductResponse;
    }

    public ClientProductResponse entityToResponse(Product product,
                                                  List<SimpleProductInventory> productInventories,
                                                  int averageRatingScore,
                                                  int countReviews,
                                                  List<ClientListedProductResponse> relatedProductResponses) {
        ClientProductResponse clientProductResponse = new ClientProductResponse();

        clientProductResponse.setProductId(product.getId());
        clientProductResponse.setProductName(product.getName());
        clientProductResponse.setProductSlug(product.getSlug());
        clientProductResponse.setProductShortDescription(product.getShortDescription());
        clientProductResponse.setProductDescription(product.getDescription());
        clientProductResponse.setProductImages(imageMapper.entityToResponse(product.getImages()));
        clientProductResponse.setProductCategory(clientCategoryMapper.entityToResponse(product.getCategory(), false));
        clientProductResponse.setProductBrand(product.getBrand() == null ? null : new ClientProductResponse.ClientBrandResponse()
                .setBrandId(product.getBrand().getId())
                .setBrandName(product.getBrand().getName()));
        clientProductResponse.setProductSpecifications(product.getSpecifications());
        clientProductResponse.setProductVariants(product.getVariants().stream()
                .map(variant -> {
                    Integer quantity = variant.getQuantity();
                    return new ClientProductResponse.ClientVariantResponse()
                            .setVariantId(variant.getId())
                            .setVariantPrice(variant.getPrice())
                            .setVariantCost(variant.getCost())
                            .setVariantSku(variant.getSku())
                            .setVariantProperties(variant.getProperties())
                            .setVariantImages(variant.getImages())
                            .setVariantStatus(variant.getStatus())
                            .setQuantity(quantity)
                            .setInventoryStatus(quantity != null && quantity > 0 ? "Còn hàng" : "Hết hàng");
                })
                .collect(Collectors.toList()));
        clientProductResponse.setProductSaleable(productInventories.stream()
                .filter(productInventory -> productInventory.getProductId().equals(product.getId()))
                .findAny()
                .map(productInventory -> productInventory.getCanBeSold() > 0)
                .orElse(false));
        clientProductResponse.setProductAverageRatingScore(averageRatingScore);
        clientProductResponse.setProductCountReviews(countReviews);
        clientProductResponse.setProductRelatedProducts(relatedProductResponses);
        clientProductResponse.setProductPromotion(promotionRepository
                .findActivePromotionByProductId(product.getId())
                .stream()
                .findFirst()
                .map(promotionMapper::entityToClientResponse)
                .orElse(null));
        clientProductResponse.setProductGuarantee(product.getGuarantee() == null ? null : new ClientProductResponse.ClientGuaranteeResponse()
                .setGuaranteeId(product.getGuarantee().getId())
                .setGuaranteeName(product.getGuarantee().getName())
                .setGuaranteeDescription(product.getGuarantee().getDescription()));

        return clientProductResponse;
    }

}

