package com.hnh.mapper.inventory;

import com.hnh.dto.inventory.WarehouseRequest;
import com.hnh.dto.inventory.WarehouseResponse;
import com.hnh.entity.inventory.Warehouse;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface WarehouseMapper extends GenericMapper<Warehouse, WarehouseRequest, WarehouseResponse> {}

