package com.healthcareapp.healthcareappbackend.converter;

import com.healthcareapp.healthcareappbackend.dto.SymptomPatientDto;
import com.healthcareapp.healthcareappbackend.entity.SymptomPatientEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SymptomPatientMapper {
    SymptomPatientMapper INSTANCE = Mappers.getMapper(SymptomPatientMapper.class);

    SymptomPatientDto entityToDto(SymptomPatientEntity entity);

    SymptomPatientEntity dtoToEntity(SymptomPatientDto dto);
}
