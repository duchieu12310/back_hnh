package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.DocketVariantEliminatedResponse;
import com.thatannhien.dto.inventory.DocketVariantExtendedResponse;
import com.thatannhien.dto.inventory.DocketVariantRequest;
import com.thatannhien.dto.inventory.DocketVariantResponse;
import com.thatannhien.entity.inventory.DocketVariant;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {DocketReasonMapper.class, WarehouseMapper.class, MapperUtils.class})
public interface DocketVariantMapper extends GenericMapper<DocketVariant, DocketVariantRequest, DocketVariantResponse> {

    @Override
    @Mapping(source = "variantId", target = "variant")
    DocketVariant requestToEntity(DocketVariantRequest request);

    @Override
    @Mapping(source = "variantId", target = "variant")
    DocketVariant partialUpdate(@MappingTarget DocketVariant entity, DocketVariantRequest request);

    DocketVariantExtendedResponse docketVariantToDocketVariantExtendedResponse(DocketVariant docketVariant);

    DocketVariantEliminatedResponse docketVariantToDocketVariantEliminatedResponse(DocketVariant docketVariant);

}
