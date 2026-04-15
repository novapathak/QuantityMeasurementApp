package com.apps.quantitymeasurement.model;

import java.util.List;

public record MeasurementTypeMetadata(
        String name,
        String label,
        boolean arithmeticSupported,
        List<String> units
) {
}
