package com.thatannhien.mapper.inventory;

import com.thatannhien.dto.inventory.WarehouseRequest;
import com.thatannhien.dto.inventory.WarehouseResponse;
import com.thatannhien.entity.inventory.Warehouse;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface WarehouseMapper extends GenericMapper<Warehouse, WarehouseRequest, WarehouseResponse> {}
