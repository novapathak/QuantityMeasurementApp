package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.core.Quantity;
import com.apps.quantitymeasurement.model.QuantityDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementEntity;
import com.apps.quantitymeasurement.repository.IQuantityMeasurementRepository;
import com.apps.quantitymeasurement.units.LengthUnit;
import com.apps.quantitymeasurement.units.TemperatureUnit;
import com.apps.quantitymeasurement.units.VolumeUnit;
import com.apps.quantitymeasurement.units.WeightUnit;

public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final IQuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(IQuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    // DTO -> Core
    private Quantity<IMeasurable> toQuantity(QuantityDTO dto) {
        IMeasurable unit = getCoreUnit(dto.unit);
        return new Quantity<>(dto.value, unit);
    }

    // DTO Unit -> Core Unit
    private IMeasurable getCoreUnit(QuantityDTO.IMeasurableUnit dtoUnit) {

        String name = dtoUnit.getUnitName();

        for (LengthUnit u : LengthUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (WeightUnit u : WeightUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (VolumeUnit u : VolumeUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        for (TemperatureUnit u : TemperatureUnit.values())
            if (u.getUnitName().equalsIgnoreCase(name))
                return u;

        throw new IllegalArgumentException("Invalid unit: " + name);
    }

    // COMMON METHOD TO SAVE DATA
    private void saveToDatabase(double value, String unit, String type, String operation, double result) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setValue(value);
        entity.setUnit(unit);
        entity.setType(type);
        entity.setOperation(operation);
        entity.setResult(result);

        repository.save(entity);
    }

    @Override
    public boolean compare(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasurable> quantity1 = toQuantity(q1);
        Quantity<IMeasurable> quantity2 = toQuantity(q2);

        boolean result = quantity1.equals(quantity2);

        //  SAVE
        saveToDatabase(q1.value, q1.unit.getUnitName(), "COMPARE", "COMPARE", result ? 1 : 0);

        return result;
    }

    @Override
    public QuantityDTO convert(QuantityDTO quantityDTO, QuantityDTO.IMeasurableUnit targetUnit) {

        Quantity<IMeasurable> quantity = toQuantity(quantityDTO);
        IMeasurable coreTargetUnit = getCoreUnit(targetUnit);

        Quantity<IMeasurable> result = quantity.convertTo(coreTargetUnit);

        //  SAVE
        saveToDatabase(quantityDTO.value, quantityDTO.unit.getUnitName(), "CONVERT", "CONVERT", result.getValue());

        return new QuantityDTO(result.getValue(), targetUnit);
    }

    @Override
    public QuantityDTO add(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasurable> quantity1 = toQuantity(q1);
        Quantity<IMeasurable> quantity2 = toQuantity(q2);

        Quantity<IMeasurable> result = quantity1.add(quantity2);

        // 🔥 CREATE ENTITY
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setValue(q1.value);
        entity.setUnit(q1.unit.getUnitName());
        entity.setType(q1.unit.getMeasurementType());
        entity.setOperation("ADD");
        entity.setResult(result.getValue());

        // 🔥 SAVE TO DB
        repository.save(entity);

        return new QuantityDTO(result.getValue(), q1.unit);
    }

    @Override
    public QuantityDTO subtract(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasurable> quantity1 = toQuantity(q1);
        Quantity<IMeasurable> quantity2 = toQuantity(q2);

        Quantity<IMeasurable> result = quantity1.subtract(quantity2);

        //  SAVE
        saveToDatabase(q1.value, q1.unit.getUnitName(), "LENGTH", "SUBTRACT", result.getValue());

        return new QuantityDTO(result.getValue(), q1.unit);
    }

    @Override
    public double divide(QuantityDTO q1, QuantityDTO q2) {

        Quantity<IMeasurable> quantity1 = toQuantity(q1);
        Quantity<IMeasurable> quantity2 = toQuantity(q2);

        double result = quantity1.divide(quantity2);

        //  SAVE
        saveToDatabase(q1.value, q1.unit.getUnitName(), "DIVIDE", "DIVIDE", result);

        return result;
    }
}