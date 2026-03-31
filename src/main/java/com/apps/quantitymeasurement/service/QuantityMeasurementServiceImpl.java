package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.core.Quantity;
import com.apps.quantitymeasurement.model.*;
import com.apps.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.apps.quantitymeasurement.units.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    @Autowired
    private QuantityMeasurementRepository repository;

    private Quantity<IMeasurable> toQuantity(QuantityDTO dto) {
        return new Quantity<>(dto.getValue(), getCoreUnit(dto.getUnit()));
    }
    
    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    private IMeasurable getCoreUnit(String unitName) {
        for (LengthUnit u : LengthUnit.values())
            if (u.getUnitName().equalsIgnoreCase(unitName)) return u;

        for (WeightUnit u : WeightUnit.values())
            if (u.getUnitName().equalsIgnoreCase(unitName)) return u;

        for (VolumeUnit u : VolumeUnit.values())
            if (u.getUnitName().equalsIgnoreCase(unitName)) return u;

        for (TemperatureUnit u : TemperatureUnit.values())
            if (u.getUnitName().equalsIgnoreCase(unitName)) return u;

        throw new IllegalArgumentException("Invalid unit: " + unitName);
    }

    private QuantityMeasurementDTO saveEntity(QuantityMeasurementEntity entity) {
        repository.save(entity);
        return QuantityMeasurementDTO.fromEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        try {
            double result = toQuantity(q1).add(toQuantity(q2)).getValue();
            e.setThisValue(q1.getValue());
            e.setThisUnit(q1.getUnit());
            e.setThisMeasurementType(q1.getMeasurementType());
            e.setThatValue(q2.getValue());
            e.setThatUnit(q2.getUnit());
            e.setThatMeasurementType(q2.getMeasurementType());
            e.setOperation("ADD");
            e.setResultValue(result);
            e.setResultUnit(q1.getUnit());
            e.setResultMeasurementType(q1.getMeasurementType());
            e.setError(false);
        } catch (Exception ex) {
            e.setError(true);
            e.setErrorMessage(ex.getMessage());
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        try {
            double result = toQuantity(q1).subtract(toQuantity(q2)).getValue();
            e.setThisValue(q1.getValue());
            e.setThisUnit(q1.getUnit());
            e.setThisMeasurementType(q1.getMeasurementType());
            e.setThatValue(q2.getValue());
            e.setThatUnit(q2.getUnit());
            e.setThatMeasurementType(q2.getMeasurementType());
            e.setOperation("SUBTRACT");
            e.setResultValue(result);
            e.setResultUnit(q1.getUnit());
            e.setResultMeasurementType(q1.getMeasurementType());
            e.setError(false);
        } catch (Exception ex) {
            e.setError(true);
            e.setErrorMessage(ex.getMessage());
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        try {
            double result = toQuantity(q1).divide(toQuantity(q2));
            e.setThisValue(q1.getValue());
            e.setThisUnit(q1.getUnit());
            e.setThisMeasurementType(q1.getMeasurementType());
            e.setThatValue(q2.getValue());
            e.setThatUnit(q2.getUnit());
            e.setThatMeasurementType(q2.getMeasurementType());
            e.setOperation("DIVIDE");
            e.setResultValue(result);
            e.setError(false);
        } catch (Exception ex) {
            e.setError(true);
            e.setErrorMessage(ex.getMessage());
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        try {
            boolean result = toQuantity(q1).equals(toQuantity(q2));
            e.setThisValue(q1.getValue());
            e.setThisUnit(q1.getUnit());
            e.setThisMeasurementType(q1.getMeasurementType());
            e.setThatValue(q2.getValue());
            e.setThatUnit(q2.getUnit());
            e.setThatMeasurementType(q2.getMeasurementType());
            e.setOperation("COMPARE");
            e.setResultString(String.valueOf(result));
            e.setError(false);
        } catch (Exception ex) {
            e.setError(true);
            e.setErrorMessage(ex.getMessage());
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = new QuantityMeasurementEntity();
        try {
            double result = toQuantity(q1)
                    .convertTo(getCoreUnit(q2.getUnit()))
                    .getValue();
            e.setThisValue(q1.getValue());
            e.setThisUnit(q1.getUnit());
            e.setThisMeasurementType(q1.getMeasurementType());
            e.setThatValue(q2.getValue());
            e.setThatUnit(q2.getUnit());
            e.setThatMeasurementType(q2.getMeasurementType());
            e.setOperation("CONVERT");
            e.setResultValue(result);
            e.setResultUnit(q2.getUnit());
            e.setResultMeasurementType(q2.getMeasurementType());
            e.setError(false);
        } catch (Exception ex) {
            e.setError(true);
            e.setErrorMessage(ex.getMessage());
        }
        return saveEntity(e);
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return repository.findByOperation(operation)
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public long countByOperation(String operation) {
        return repository.countByOperationAndIsErrorFalse(operation);
    }
}