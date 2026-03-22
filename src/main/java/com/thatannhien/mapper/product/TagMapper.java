package com.thatannhien.mapper.product;

import com.thatannhien.dto.product.TagRequest;
import com.thatannhien.dto.product.TagResponse;
import com.thatannhien.entity.product.Tag;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TagMapper extends GenericMapper<Tag, TagRequest, TagResponse> {
}
