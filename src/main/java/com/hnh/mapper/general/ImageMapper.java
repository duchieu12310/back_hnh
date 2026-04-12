package com.hnh.mapper.general;

import com.hnh.dto.general.ImageRequest;
import com.hnh.dto.general.ImageResponse;
import com.hnh.entity.general.Image;
import com.hnh.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends GenericMapper<Image, ImageRequest, ImageResponse> {}

