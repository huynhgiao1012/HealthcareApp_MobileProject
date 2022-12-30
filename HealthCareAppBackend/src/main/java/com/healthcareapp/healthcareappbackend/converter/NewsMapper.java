package com.healthcareapp.healthcareappbackend.converter;

import com.healthcareapp.healthcareappbackend.dto.NewsDto;
import com.healthcareapp.healthcareappbackend.entity.NewsEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface NewsMapper {
    NewsMapper INSTANCE = Mappers.getMapper(NewsMapper.class);

    NewsDto entityToDto(NewsEntity news);

    NewsEntity dtoToEntity(NewsDto newsDto);
}
