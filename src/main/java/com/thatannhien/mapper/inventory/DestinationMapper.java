package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.DestinationRequest;
import com.thatannhien.dto.inventory.DestinationResponse;
import com.thatannhien.entity.inventory.Destination;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface DestinationMapper extends GenericMapper<Destination, DestinationRequest, DestinationResponse> {}
