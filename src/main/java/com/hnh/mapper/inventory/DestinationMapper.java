package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.DestinationRequest;
import com.hnh.dto.inventory.DestinationResponse;
import com.hnh.entity.inventory.Destination;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface DestinationMapper extends GenericMapper<Destination, DestinationRequest, DestinationResponse> {}

