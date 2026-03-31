package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO convert(QuantityDTO q1, QuantityDTO q2);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    long countByOperation(String operation);
}