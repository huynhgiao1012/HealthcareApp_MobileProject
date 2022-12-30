package com.healthcareapp.healthcareappbackend.entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
public class SymptomPatientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "symptom_id", nullable = false)
    private SymptomEntity symptom;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private AccountEntity account;
}
