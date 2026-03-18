package com.apps.quantitymeasurement.repository;

import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;

import java.util.List;

public interface IQuantityMeasurementRepository {

    // ✅ Save a measurement
    void save(QuantityMeasurementEntity entity);

    // ✅ Get all stored measurements
    List<QuantityMeasurementEntity> getAllMeasurements();

    // ✅ Filter by operation (e.g., ADD, COMPARE)
    List<QuantityMeasurementEntity> getMeasurementsByOperation(String operation);

    // ✅ Filter by type (e.g., LENGTH, WEIGHT)
    List<QuantityMeasurementEntity> getMeasurementsByType(String type);

    // ✅ Count total records
    int getTotalCount();

    // ✅ Delete all records (used in testing/reset)
    void deleteAll();

    // ✅ Optional: Pool statistics (for DB repo only)
    default String getPoolStatistics() {
        return "Not supported";
    }

    // ✅ Optional: Clean up resources
    default void releaseResources() {
        // default no-op
    }
}