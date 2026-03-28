package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.DocketReasonRequest;
import com.hnh.dto.inventory.DocketReasonResponse;
import com.hnh.entity.inventory.DocketReason;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface DocketReasonMapper extends GenericMapper<DocketReason, DocketReasonRequest, DocketReasonResponse> {}

