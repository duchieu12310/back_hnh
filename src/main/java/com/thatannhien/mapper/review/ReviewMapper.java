package com.thatannhien.mapper.review;

import com.thatannhien.dto.review.ReviewRequest;
import com.thatannhien.dto.review.ReviewResponse;
import com.thatannhien.entity.review.Review;
import com.thatannhien.mapper.GenericMapper;
import com.thatannhien.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface ReviewMapper extends GenericMapper<Review, ReviewRequest, ReviewResponse> {

    @Override
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "productId", target = "product")
    Review requestToEntity(ReviewRequest request);

    @Override
    @Mapping(source = "userId", target = "user")
    @Mapping(source = "productId", target = "product")
    Review partialUpdate(@MappingTarget Review entity, ReviewRequest request);

}
