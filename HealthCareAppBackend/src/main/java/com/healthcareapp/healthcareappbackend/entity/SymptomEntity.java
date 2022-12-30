package com.healthcareapp.healthcareappbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
public class SymptomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "symptom")
    private List<SymptomPatientEntity> symptomPatientEntityList;

}
