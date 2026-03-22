package com.thatannhien.mapper.client;

import com.thatannhien.dto.client.ClientOrderDetailResponse;
import com.thatannhien.dto.client.ClientOrderRequest;
import com.thatannhien.dto.client.ClientOrderVariantResponse;
import com.thatannhien.dto.client.ClientSimpleOrderResponse;
import com.thatannhien.dto.client.ClientWaybillResponse;
import com.thatannhien.entity.general.Image;
import com.thatannhien.entity.order.Order;
import com.thatannhien.entity.order.OrderVariant;
import com.thatannhien.entity.waybill.WaybillLog;
import com.thatannhien.repository.review.ReviewRepository;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public abstract class ClientOrderMapper {

    @Autowired
    private ReviewRepository reviewRepository;

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createdAt", target = "orderCreatedAt")
    @Mapping(source = "code", target = "orderCode")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "totalPay", target = "orderTotalPay")
    @Mapping(source = "orderVariants", target = "orderItems")
    @Mapping(source = "paymentStatus", target = "orderPaymentStatus")
    public abstract ClientSimpleOrderResponse entityToResponse(Order order);

    @Mapping(source = "id", target = "orderId")
    @Mapping(source = "createdAt", target = "orderCreatedAt")
    @Mapping(source = "code", target = "orderCode")
    @Mapping(source = "status", target = "orderStatus")
    @Mapping(source = "toName", target = "orderToName")
    @Mapping(source = "toPhone", target = "orderToPhone")
    @Mapping(source = "toAddress", target = "orderToAddress")
    @Mapping(source = "toWardName", target = "orderToWardName")
    @Mapping(source = "toDistrictName", target = "orderToDistrictName")
    @Mapping(source = "toProvinceName", target = "orderToProvinceName")
    @Mapping(source = "totalAmount", target = "orderTotalAmount")
    @Mapping(source = "tax", target = "orderTax")
    @Mapping(source = "shippingCost", target = "orderShippingCost")
    @Mapping(source = "totalPay", target = "orderTotalPay")
    @Mapping(source = "paymentMethodType", target = "orderPaymentMethodType")
    @Mapping(source = "paymentStatus", target = "orderPaymentStatus")
    @Mapping(source = "orderVariants", target = "orderItems")
    @Mapping(source = "waybill", target = "orderWaybill")
    @Mapping(source = "waybill.code", target = "orderWaybill.waybillCode")
    @Mapping(source = "waybill.expectedDeliveryTime", target = "orderWaybill.waybillExpectedDeliveryTime")
    @Mapping(source = "waybill.waybillLogs", target = "orderWaybill.waybillLogs")
    public abstract ClientOrderDetailResponse entityToDetailResponse(Order order);

    @Mapping(source = "variant.id", target = "orderItemVariant.variantId")
    @Mapping(source = "variant.product.id", target = "orderItemVariant.variantProduct.productId")
    @Mapping(source = "variant.product.name", target = "orderItemVariant.variantProduct.productName")
    @Mapping(source = "variant.product.slug", target = "orderItemVariant.variantProduct.productSlug")
    @Mapping(source = "variant.product.images", target = "orderItemVariant.variantProduct.productThumbnail")
    @Mapping(source = "variant.properties", target = "orderItemVariant.variantProperties")
    @Mapping(source = "price", target = "orderItemPrice")
    @Mapping(source = "quantity", target = "orderItemQuantity")
    @Mapping(source = "amount", target = "orderItemAmount")
    public abstract ClientOrderVariantResponse entityToResponse(OrderVariant orderVariant);

    public String mapToProductThumbnail(List<Image> images) {
        return images.stream().filter(Image::getIsThumbnail).findAny().map(Image::getPath).orElse(null);
    }

    @AfterMapping
    public ClientOrderDetailResponse entityToDetailResponseCallback(@MappingTarget ClientOrderDetailResponse clientOrderDetailResponse) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        // Kiểm tra authentication và username trước khi sử dụng
        if (authentication != null && authentication.getName() != null && !authentication.getName().equals("anonymousUser")) {
            String username = authentication.getName();

            for (var clientOrderVariantResponse : clientOrderDetailResponse.getOrderItems()) {
                var productId = clientOrderVariantResponse.getOrderItemVariant().getVariantProduct().getProductId();
                var productIsReviewed = reviewRepository.existsByProductIdAndUsername(productId, username);
                clientOrderVariantResponse.getOrderItemVariant().getVariantProduct().setProductIsReviewed(productIsReviewed);
            }
        } else {
            // Nếu không có authentication, set tất cả productIsReviewed = false
            for (var clientOrderVariantResponse : clientOrderDetailResponse.getOrderItems()) {
                clientOrderVariantResponse.getOrderItemVariant().getVariantProduct().setProductIsReviewed(false);
            }
        }

        return clientOrderDetailResponse;
    }

    @BeanMapping(qualifiedByName = "attachOrder")
    @Mapping(source = "orderResourceId", target = "orderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason")
    @Mapping(source = "userId", target = "user")
    public abstract Order requestToEntity(ClientOrderRequest request);

    @BeanMapping(qualifiedByName = "attachOrder")
    @Mapping(source = "orderResourceId", target = "orderResource")
    @Mapping(source = "orderCancellationReasonId", target = "orderCancellationReason")
    @Mapping(source = "userId", target = "user")
    public abstract Order partialUpdate(@MappingTarget Order entity, ClientOrderRequest request);

    @Mapping(source = "id", target = "waybillLogId")
    @Mapping(source = "createdAt", target = "waybillLogCreatedAt")
    @Mapping(source = "previousStatus", target = "waybillLogPreviousStatus")
    @Mapping(source = "currentStatus", target = "waybillLogCurrentStatus")
    public abstract ClientWaybillResponse.ClientWaybillLogResponse entityToResponse(WaybillLog waybillLog);

}
