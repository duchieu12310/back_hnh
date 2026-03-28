package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.VariantInventoryLimitRequest;
import com.hnh.dto.inventory.VariantInventoryLimitResponse;
import com.hnh.entity.inventory.VariantInventoryLimit;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface VariantInventoryLimitMapper extends GenericMapper<VariantInventoryLimit, VariantInventoryLimitRequest,
        VariantInventoryLimitResponse> {

    @Override
    @Mapping(source = "variantId", target = "variant")
    VariantInventoryLimit requestToEntity(VariantInventoryLimitRequest request);

    @Override
    @Mapping(source = "variantId", target = "variant")
    VariantInventoryLimit partialUpdate(@MappingTarget VariantInventoryLimit entity, VariantInventoryLimitRequest request);

}

