package com.hnh.mapper.chat;

import com.hnh.dto.chat.RoomRequest;
import com.hnh.dto.chat.RoomResponse;
import com.hnh.entity.chat.Room;
import com.hnh.mapper.GenericMapper;
import com.hnh.utils.MapperUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = MapperUtils.class)
public interface RoomMapper extends GenericMapper<Room, RoomRequest, RoomResponse> {

    @Override
    @Mapping(source = "userId", target = "user")
    Room requestToEntity(RoomRequest request);

}

