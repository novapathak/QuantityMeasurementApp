package com.apps.quantitymeasurement.service;

import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.core.Quantity;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.*;
import com.apps.quantitymeasurement.repository.QuantityMeasurementRepository;
import com.apps.quantitymeasurement.util.MeasurementUnitRegistry;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class QuantityMeasurementServiceImpl implements IQuantityMeasurementService {

    private final QuantityMeasurementRepository repository;

    public QuantityMeasurementServiceImpl(QuantityMeasurementRepository repository) {
        this.repository = repository;
    }

    @FunctionalInterface
    private interface QuantityBinaryOperation {
        double apply(Quantity<IMeasurable> left, Quantity<IMeasurable> right, IMeasurable targetUnit);
    }

    private Quantity<IMeasurable> toQuantity(QuantityDTO dto) {
        if (dto == null) {
            throw new QuantityMeasurementException("Quantity input is required");
        }
        return new Quantity<>(dto.getValue(), getCoreUnit(dto.getUnit()));
    }

    private IMeasurable getCoreUnit(String unitName) {
        return MeasurementUnitRegistry.resolveUnit(unitName)
                .orElseThrow(() -> new QuantityMeasurementException("Invalid unit: " + unitName));
    }

    private QuantityMeasurementDTO saveEntity(QuantityMeasurementEntity entity) {
        return QuantityMeasurementDTO.fromEntity(repository.save(entity));
    }

    private QuantityMeasurementEntity buildEntity(QuantityDTO thisQuantity, QuantityDTO thatQuantity, OperationType operation) {
        QuantityMeasurementEntity entity = new QuantityMeasurementEntity();
        entity.setThisValue(thisQuantity.getValue());
        entity.setThisUnit(normalizeUnit(thisQuantity.getUnit()));
        entity.setThisMeasurementType(resolveMeasurementType(thisQuantity));
        entity.setOperation(operation.name());
        entity.setError(false);

        if (thatQuantity != null) {
            entity.setThatValue(thatQuantity.getValue());
            entity.setThatUnit(normalizeUnit(thatQuantity.getUnit()));
            entity.setThatMeasurementType(resolveMeasurementType(thatQuantity));
        }

        return entity;
    }

    private String normalizeUnit(String unit) {
        if (!StringUtils.hasText(unit)) {
            return unit;
        }
        return MeasurementUnitRegistry.canonicalUnitName(unit).orElse(unit.toUpperCase());
    }

    private String resolveMeasurementType(QuantityDTO quantity) {
        return MeasurementUnitRegistry.canonicalMeasurementType(quantity.getMeasurementType())
                .or(() -> MeasurementUnitRegistry.measurementTypeForUnit(quantity.getUnit()))
                .orElse(quantity.getMeasurementType());
    }

    private String resolveTargetUnit(String targetUnit, QuantityDTO fallbackQuantity, QuantityDTO sourceQuantity) {
        if (StringUtils.hasText(targetUnit)) {
            return normalizeUnit(targetUnit);
        }
        if (fallbackQuantity != null && StringUtils.hasText(fallbackQuantity.getUnit())) {
            return normalizeUnit(fallbackQuantity.getUnit());
        }
        if (sourceQuantity != null && StringUtils.hasText(sourceQuantity.getUnit())) {
            return normalizeUnit(sourceQuantity.getUnit());
        }
        throw new QuantityMeasurementException("Target unit is required");
    }

    private void saveErrorAndThrow(QuantityMeasurementEntity entity, Exception ex, OperationType operation, boolean badRequest) {
        entity.setError(true);
        entity.setErrorMessage(ex.getMessage());
        repository.save(entity);

        if (badRequest) {
            throw new QuantityMeasurementException(operation.name() + " Error: " + ex.getMessage(), ex);
        }

        if (ex instanceof ArithmeticException arithmeticException) {
            throw arithmeticException;
        }

        throw new RuntimeException(ex);
    }

    private QuantityMeasurementDTO executeQuantityOperation(
            QuantityDTO q1,
            QuantityDTO q2,
            String targetUnit,
            OperationType operationType,
            QuantityBinaryOperation operation
    ) {
        QuantityMeasurementEntity entity = buildEntity(q1, q2, operationType);
        try {
            String resolvedTargetUnit = resolveTargetUnit(targetUnit, null, q1);
            IMeasurable targetCoreUnit = getCoreUnit(resolvedTargetUnit);
            double result = operation.apply(toQuantity(q1), toQuantity(q2), targetCoreUnit);
            entity.setResultValue(result);
            entity.setResultUnit(resolvedTargetUnit);
            entity.setResultMeasurementType(
                    MeasurementUnitRegistry.measurementTypeForUnit(resolvedTargetUnit).orElse(resolveMeasurementType(q1))
            );
        } catch (Exception ex) {
            saveErrorAndThrow(entity, ex, operationType, true);
        }
        return saveEntity(entity);
    }

    @Override
    public QuantityMeasurementDTO add(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        return executeQuantityOperation(
                q1,
                q2,
                targetUnit,
                OperationType.ADD,
                (left, right, resolvedTarget) -> left.add(right, resolvedTarget).getValue()
        );
    }

    @Override
    public QuantityMeasurementDTO subtract(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        return executeQuantityOperation(
                q1,
                q2,
                targetUnit,
                OperationType.SUBTRACT,
                (left, right, resolvedTarget) -> left.subtract(right, resolvedTarget).getValue()
        );
    }

    @Override
    public QuantityMeasurementDTO multiply(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        return executeQuantityOperation(
                q1,
                q2,
                targetUnit,
                OperationType.MULTIPLY,
                (left, right, resolvedTarget) -> left.multiply(right, resolvedTarget).getValue()
        );
    }

    @Override
    public QuantityMeasurementDTO divide(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = buildEntity(q1, q2, OperationType.DIVIDE);
        try {
            double result = toQuantity(q1).divide(toQuantity(q2));
            e.setResultValue(result);
        } catch (ArithmeticException ex) {
            saveErrorAndThrow(e, ex, OperationType.DIVIDE, false);
        } catch (Exception ex) {
            saveErrorAndThrow(e, ex, OperationType.DIVIDE, true);
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO compare(QuantityDTO q1, QuantityDTO q2) {
        QuantityMeasurementEntity e = buildEntity(q1, q2, OperationType.COMPARE);
        try {
            boolean result = toQuantity(q1).equals(toQuantity(q2));
            e.setResultString(String.valueOf(result));
        } catch (Exception ex) {
            saveErrorAndThrow(e, ex, OperationType.COMPARE, true);
        }
        return saveEntity(e);
    }

    @Override
    public QuantityMeasurementDTO convert(QuantityDTO q1, QuantityDTO q2, String targetUnit) {
        QuantityMeasurementEntity e = buildEntity(q1, q2, OperationType.CONVERT);
        try {
            String resolvedTargetUnit = resolveTargetUnit(targetUnit, q2, null);
            double result = toQuantity(q1).convertTo(getCoreUnit(resolvedTargetUnit)).getValue();
            e.setResultValue(result);
            e.setResultUnit(resolvedTargetUnit);
            e.setResultMeasurementType(MeasurementUnitRegistry.measurementTypeForUnit(resolvedTargetUnit).orElse(resolveMeasurementType(q1)));
        } catch (Exception ex) {
            saveErrorAndThrow(e, ex, OperationType.CONVERT, true);
        }
        return saveEntity(e);
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByOperation(String operation) {
        return repository.findByOperationIgnoreCase(OperationType.fromValue(operation).name())
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    @Override
    public List<QuantityMeasurementDTO> getHistoryByMeasurementType(String measurementType) {
        String normalizedMeasurementType = MeasurementUnitRegistry.canonicalMeasurementType(measurementType)
                .orElseThrow(() -> new QuantityMeasurementException("Invalid measurement type: " + measurementType));

        return repository.findByThisMeasurementTypeIgnoreCase(normalizedMeasurementType)
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    @Override
    public List<QuantityMeasurementDTO> getErroredHistory() {
        return repository.findByIsErrorTrue()
                .stream()
                .map(QuantityMeasurementDTO::fromEntity)
                .toList();
    }

    @Override
    public long countByOperation(String operation) {
        return repository.countByOperationIgnoreCaseAndIsErrorFalse(OperationType.fromValue(operation).name());
    }
}
