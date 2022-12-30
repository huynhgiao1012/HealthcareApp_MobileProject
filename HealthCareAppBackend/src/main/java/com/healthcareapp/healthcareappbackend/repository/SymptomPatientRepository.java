package com.healthcareapp.healthcareappbackend.repository;

import com.healthcareapp.healthcareappbackend.entity.AccountEntity;
import com.healthcareapp.healthcareappbackend.entity.SymptomPatientEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface SymptomPatientRepository extends CrudRepository<SymptomPatientEntity, Long> {
    @Override
    SymptomPatientEntity save(SymptomPatientEntity entity);
}
