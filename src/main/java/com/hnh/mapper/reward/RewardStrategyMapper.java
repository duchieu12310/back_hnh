package com.hnh.mapper.reward;

import com.hnh.dto.reward.RewardStrategyRequest;
import com.hnh.dto.reward.RewardStrategyResponse;
import com.hnh.entity.reward.RewardStrategy;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RewardStrategyMapper extends GenericMapper<RewardStrategy, RewardStrategyRequest, RewardStrategyResponse> {

    @Override
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RewardStrategy partialUpdate(@MappingTarget RewardStrategy entity, RewardStrategyRequest request);

}

