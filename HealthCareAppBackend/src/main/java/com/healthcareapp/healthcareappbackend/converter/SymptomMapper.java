package com.healthcareapp.healthcareappbackend.converter;

import com.healthcareapp.healthcareappbackend.dto.SymptomDto;
import com.healthcareapp.healthcareappbackend.entity.SymptomEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SymptomMapper {
    SymptomMapper INSTANCE = Mappers.getMapper(SymptomMapper.class);

    SymptomDto entityToDto(SymptomEntity entity);

    SymptomEntity dtoToEntity(SymptomDto dto);
}
