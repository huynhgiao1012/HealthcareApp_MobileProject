package com.healthcareapp.healthcareappbackend.service;

import com.healthcareapp.healthcareappbackend.converter.SymptomPatientMapper;
import com.healthcareapp.healthcareappbackend.dto.SymptomPatientDto;
import com.healthcareapp.healthcareappbackend.entity.SymptomEntity;
import com.healthcareapp.healthcareappbackend.entity.SymptomPatientEntity;
import com.healthcareapp.healthcareappbackend.repository.AccountRepository;
import com.healthcareapp.healthcareappbackend.repository.SymptomPatientRepository;
import com.healthcareapp.healthcareappbackend.repository.SymptomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SymptomPatientService {
    @Autowired
    private SymptomPatientRepository symptomPatientRepository;

    @Autowired
    private SymptomRepository symptomRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private SymptomPatientMapper symptomPatientMapper;

    public SymptomPatientDto save(SymptomPatientDto symptomPatientDto) {
        SymptomPatientEntity symptomPatient = symptomPatientMapper.INSTANCE.dtoToEntity(symptomPatientDto);
        if (symptomRepository.findByName(symptomPatientDto.getSymptomName()).isEmpty()) {
            SymptomEntity symptomEntity = new SymptomEntity();
            symptomEntity.setName(symptomPatientDto.getSymptomName());
            symptomEntity.setDescription(symptomPatientDto.getSymptomDescription());
            symptomRepository.save(symptomEntity);
        }

        symptomPatient.setSymptom(symptomRepository.findByName(symptomPatientDto.getSymptomName()).get());
        symptomPatient.setAccount(accountRepository.findByIdCard(symptomPatientDto.getPatientIdCard()).get());

        return symptomPatientMapper.INSTANCE.entityToDto(symptomPatientRepository.save(symptomPatient));
    }
}
