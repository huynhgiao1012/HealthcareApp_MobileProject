package com.healthcareapp.healthcareappbackend.dto;

import lombok.*;

@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SymptomPatientDto {
    private Long id;
    private String symptomName;
    private String patientIdCard;
    private String symptomDescription;
}
