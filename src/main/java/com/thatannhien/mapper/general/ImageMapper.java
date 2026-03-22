package com.thatannhien.mapper.general;

import com.thatannhien.dto.general.ImageRequest;
import com.thatannhien.dto.general.ImageResponse;
import com.thatannhien.entity.general.Image;
import com.thatannhien.mapper.GenericMapper;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ImageMapper extends GenericMapper<Image, ImageRequest, ImageResponse> {}
