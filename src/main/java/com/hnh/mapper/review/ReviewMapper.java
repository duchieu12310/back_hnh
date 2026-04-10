package com.hnh.mapper.review;

import com.hnh.dto.review.ReviewRequest;
import com.hnh.dto.review.ReviewResponse;
import com.hnh.entity.review.Review;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface ReviewMapper extends GenericMapper<Review, ReviewRequest, ReviewResponse> {

    @Override
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapToUser")
    @Mapping(source = "productId", target = "product", qualifiedByName = "mapToProduct")
    Review requestToEntity(ReviewRequest request);

    @Override
    @Mapping(source = "userId", target = "user", qualifiedByName = "mapToUser")
    @Mapping(source = "productId", target = "product", qualifiedByName = "mapToProduct")
    Review partialUpdate(@MappingTarget Review entity, ReviewRequest request);

}
