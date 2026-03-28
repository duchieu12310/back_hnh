package com.hnh.mapper.product;

import com.hnh.dto.product.TagRequest;
import com.hnh.dto.product.TagResponse;
import com.hnh.entity.product.Tag;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends GenericMapper<Tag, TagRequest, TagResponse> {
}

