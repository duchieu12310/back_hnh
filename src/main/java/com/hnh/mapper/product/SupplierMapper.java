package com.hnh.mapper.product;

import com.hnh.dto.product.SupplierRequest;
import com.hnh.dto.product.SupplierResponse;
import com.hnh.entity.product.Supplier;
import com.hnh.mapper.GenericMapper;
import com.hnh.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface SupplierMapper extends GenericMapper<Supplier, SupplierRequest, SupplierResponse> {
}

