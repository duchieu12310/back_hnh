package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.BrandRequest;
import com.thatannhien.dto.product.BrandResponse;
import com.thatannhien.entity.product.Brand;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper extends GenericMapper<Brand, BrandRequest, BrandResponse> {}
