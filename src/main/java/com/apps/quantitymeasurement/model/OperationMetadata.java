package com.apps.quantitymeasurement.model;

public record OperationMetadata(
        String name,
        String label,
        boolean requiresSecondQuantity,
        boolean allowsTargetUnit,
        boolean requiresTargetUnit
) {
}
