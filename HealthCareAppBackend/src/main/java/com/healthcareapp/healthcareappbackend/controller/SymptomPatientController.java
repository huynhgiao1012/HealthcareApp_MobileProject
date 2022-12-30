package com.healthcareapp.healthcareappbackend.controller;

import com.healthcareapp.healthcareappbackend.dto.SymptomPatientDto;
import com.healthcareapp.healthcareappbackend.service.SymptomPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/symptomPatient")
public class SymptomPatientController {
    @Autowired
    private SymptomPatientService symptomPatientService;

    @PostMapping(path = "/saveSymptomPatient")
    public SymptomPatientDto saveSymptomPatient(@RequestParam String symptomName, @RequestParam String patientIdCard, @RequestParam String symptomDescription) {
        SymptomPatientDto symptomPatientDto = new SymptomPatientDto();
        symptomPatientDto.setPatientIdCard(patientIdCard);
        symptomPatientDto.setSymptomName(symptomName);
        symptomPatientDto.setSymptomDescription(symptomDescription);
        return symptomPatientService.save(symptomPatientDto);
    }
}
