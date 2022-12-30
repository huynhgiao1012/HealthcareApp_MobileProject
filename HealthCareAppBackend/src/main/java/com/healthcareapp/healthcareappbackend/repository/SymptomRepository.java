package com.healthcareapp.healthcareappbackend.repository;

import com.healthcareapp.healthcareappbackend.entity.AccountEntity;
import com.healthcareapp.healthcareappbackend.entity.SymptomEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@Transactional
public interface SymptomRepository extends CrudRepository<SymptomEntity, Long> {
    @Override
    SymptomEntity save(SymptomEntity entity);

    @Override
    Optional<SymptomEntity> findById(Long aLong);

    @Query(value = "select s from SymptomEntity s where s.name=?1", nativeQuery = false)
    Optional<SymptomEntity> findByName(String name);
}
