package com.thatannhien.mapper.order;

import com.thatannhien.dto.order.OrderVariantRequest;
import com.thatannhien.dto.order.OrderVariantResponse;
import com.thatannhien.entity.order.OrderVariant;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface OrderVariantMapper extends GenericMapper<OrderVariant, OrderVariantRequest, OrderVariantResponse> {

    @AfterMapping
    default void setInventoryStatus(@MappingTarget OrderVariantResponse response, OrderVariant entity) {
        if (response.getVariant() != null && entity.getVariant() != null) {
            if (entity.getVariant().getQuantity() != null && entity.getVariant().getQuantity() > 0) {
                response.getVariant().setInventoryStatus("Còn hàng");
                response.getVariant().setQuantity(entity.getVariant().getQuantity());
            } else {
                response.getVariant().setInventoryStatus("Hết hàng");
                response.getVariant().setQuantity(entity.getVariant().getQuantity());
            }
        }
    }

    @Override
    @Mapping(source = "variantId", target = "variant")
    OrderVariant requestToEntity(OrderVariantRequest request);

    @Override
    @Mapping(source = "variantId", target = "variant")
    OrderVariant partialUpdate(@MappingTarget OrderVariant entity, OrderVariantRequest request);

}
