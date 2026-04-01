package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuantityMeasurementRepository
        extends JpaRepository<QuantityMeasurementEntity, Long> {

    List<QuantityMeasurementEntity> findByOperationIgnoreCase(String operation);

    List<QuantityMeasurementEntity> findByThisMeasurementTypeIgnoreCase(String measurementType);

    List<QuantityMeasurementEntity> findByCreatedAtAfter(LocalDateTime createdAt);

    List<QuantityMeasurementEntity> findByIsErrorTrue();

    @Query("select measurement from QuantityMeasurementEntity measurement " +
            "where upper(measurement.operation) = upper(:operation) and measurement.isError = false")
    List<QuantityMeasurementEntity> findSuccessfulByOperation(@Param("operation") String operation);

    long countByOperationIgnoreCaseAndIsErrorFalse(String operation);
}
