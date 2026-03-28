package com.hnh.mapper.product;

import com.hnh.dto.product.VariantRequest;
import com.hnh.dto.product.VariantResponse;
import com.hnh.entity.product.Variant;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface VariantMapper extends GenericMapper<Variant, VariantRequest, VariantResponse> {

    @AfterMapping
    default void setInventoryStatus(@MappingTarget VariantResponse response, Variant entity) {
        if (entity.getQuantity() != null && entity.getQuantity() > 0) {
            response.setInventoryStatus("Còn hàng");
        } else {
            response.setInventoryStatus("Hết hàng");
        }
    }
}

