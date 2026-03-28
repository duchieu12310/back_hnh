package com.hnh.mapper.product;

import com.hnh.dto.product.BrandRequest;
import com.hnh.dto.product.BrandResponse;
import com.hnh.entity.product.Brand;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BrandMapper extends GenericMapper<Brand, BrandRequest, BrandResponse> {}

