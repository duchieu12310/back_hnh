package com.hnh.mapper.product;

import com.hnh.dto.inventory.VariantInventoryResponse;
import com.hnh.projection.inventory.VariantInventory;
import com.hnh.mapper.inventory.DocketVariantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = DocketVariantMapper.class)
public interface VariantInventoryMapper {

    VariantInventoryResponse toResponse(VariantInventory variantInventory);

    List<VariantInventoryResponse> toResponse(List<VariantInventory> variantInventory);

}

