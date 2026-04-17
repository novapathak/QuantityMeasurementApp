package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MeasurementTypeMetadata(
        @Schema(example = "LengthUnit")
        String name,
        @Schema(example = "Length")
        String label,
        @Schema(description = "Indicates if arithmetic operations are allowed")
        boolean arithmeticSupported,
        @Schema(description = "Supported units for this measurement type")
        List<String> units
) {
}
