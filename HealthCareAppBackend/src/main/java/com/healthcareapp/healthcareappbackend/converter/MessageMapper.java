package com.healthcareapp.healthcareappbackend.converter;

import com.healthcareapp.healthcareappbackend.dto.MessageDto;
import com.healthcareapp.healthcareappbackend.entity.MessageEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MessageMapper {
    MessageMapper INSTANCE = Mappers.getMapper(MessageMapper.class);
    MessageDto messageEntityToMessageDto(MessageEntity message);
}
