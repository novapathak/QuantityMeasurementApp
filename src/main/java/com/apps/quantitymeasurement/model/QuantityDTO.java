package com.apps.quantitymeasurement.model;

import com.apps.quantitymeasurement.util.MeasurementUnitRegistry;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityDTO {

    @NotNull(message = "Value is required")
    private Double value;

    @NotBlank(message = "Unit is required")
    private String unit;

    @NotBlank(message = "Measurement type is required")
    private String measurementType;

    @JsonIgnore
    @AssertTrue(message = "Unit must be valid for the specified measurement type")
    public boolean isUnitValidForMeasurementType() {
        if (unit == null || unit.isBlank() || measurementType == null || measurementType.isBlank()) {
            return true;
        }

        return MeasurementUnitRegistry.isValidUnitForMeasurementType(unit, measurementType);
    }
}
