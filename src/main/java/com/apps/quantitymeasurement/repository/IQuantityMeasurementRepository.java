package com.apps.quantitymeasurement.repository;
import java.util.*;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;

public interface IQuantityMeasurementRepository {

    void save(QuantityMeasurementEntity entity);

    List<QuantityMeasurementEntity> findAll();

}