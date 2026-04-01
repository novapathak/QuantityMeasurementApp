package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;

import java.util.List;

public interface IQuantityMeasurementService {

    default QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2) {
        return add(q1, q2, null);
    }

    QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2, String targetUnit);

    default QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        return subtract(q1, q2, null);
    }

    QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2, String targetUnit);

    default QuantityMeasurementDTO multiply(QuantityDTO q1, QuantityDTO q2) {
        return multiply(q1, q2, null);
    }

    QuantityMeasurementDTO multiply(QuantityDTO q1, QuantityDTO q2, String targetUnit);

    QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2);

    QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2);

    default QuantityMeasurementDTO convert(QuantityDTO q1, QuantityDTO q2) {
        return convert(q1, q2, null);
    }

    QuantityMeasurementDTO convert(QuantityDTO q1, QuantityDTO q2, String targetUnit);

    List<QuantityMeasurementDTO> getHistoryByOperation(String operation);

    List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType);

    List<QuantityMeasurementDTO> getErroredHistory();

    long countByOperation(String operation);
}
