package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.TransferRequest;
import com.hnh.dto.inventory.TransferResponse;
import com.hnh.entity.inventory.Transfer;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = DocketMapper.class)
public interface TransferMapper extends GenericMapper<Transfer, TransferRequest, TransferResponse> {}

