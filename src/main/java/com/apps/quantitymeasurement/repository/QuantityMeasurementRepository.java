package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperationIgnoreCase(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementTypeIgnoreCase(String measurementType);

    List<QuantityMeasurementEntity> findByIsErrorTrue();


    long countByOperationIgnoreCaseAndIsErrorFalse(String operation);
}
