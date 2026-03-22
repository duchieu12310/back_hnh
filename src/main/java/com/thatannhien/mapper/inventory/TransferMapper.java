package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.TransferRequest;
import com.thatannhien.dto.inventory.TransferResponse;
import com.thatannhien.entity.inventory.Transfer;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = DocketMapper.class)
public interface TransferMapper extends GenericMapper<Transfer, TransferRequest, TransferResponse> {}
