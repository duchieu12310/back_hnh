package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.SupplierRequest;
import com.thatannhien.dto.product.SupplierResponse;
import com.thatannhien.entity.product.Supplier;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.mapper.address.AddressMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = AddressMapper.class)
public interface SupplierMapper extends GenericMapper<Supplier, SupplierRequest, SupplierResponse> {
}
