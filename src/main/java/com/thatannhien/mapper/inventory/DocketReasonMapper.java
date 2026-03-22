package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.DocketReasonRequest;
import com.thatannhien.dto.inventory.DocketReasonResponse;
import com.thatannhien.entity.inventory.DocketReason;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocketReasonMapper extends GenericMapper<DocketReason, DocketReasonRequest, DocketReasonResponse> {}
