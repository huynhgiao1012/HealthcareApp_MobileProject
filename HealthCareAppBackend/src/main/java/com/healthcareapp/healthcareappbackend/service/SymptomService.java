package com.healthcareapp.healthcareappbackend.service;

import com.healthcareapp.healthcareappbackend.converter.SymptomMapper;
import com.healthcareapp.healthcareappbackend.dto.SymptomDto;
import com.healthcareapp.healthcareappbackend.entity.SymptomEntity;
import com.healthcareapp.healthcareappbackend.repository.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SymptomService {
    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private SymptomMapper symptomMapper;

    public SymptomDto save(SymptomDto symptomDto) {
        SymptomEntity symptom = symptomMapper.INSTANCE.dtoToEntity(symptomDto);
        return symptomMapper.INSTANCE.entityToDto(symptomRepository.save(symptom));
    }
}
