package com.healthcareapp.healthcareappbackend.controller;

import com.healthcareapp.healthcareappbackend.dto.SymptomDto;
import com.healthcareapp.healthcareappbackend.service.SymptomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/symptom")
public class SymptomController {
    @Autowired
    private SymptomService symptomService;

    @PostMapping(path = "/save")
    public SymptomDto createSymptom(@RequestBody SymptomDto symptomDto) {
        return symptomService.save(symptomDto);
    }
}
