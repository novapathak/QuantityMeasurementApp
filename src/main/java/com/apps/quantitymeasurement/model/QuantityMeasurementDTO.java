package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementDTO {

    @Schema(example = "1.0")
    private Double thisValue;
    @Schema(example = "FEET")
    private String thisUnit;
    @Schema(example = "LengthUnit")
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    @Schema(example = "ADD")
    private String operation;

    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    @Schema(example = "true", description = "Used by compare operations")
    private String resultString;

    private String errorMessage;
    @Schema(description = "True when the operation failed and was saved as an error")
    private boolean error;

    public static QuantityMeasurementDTO fromEntity(QuantityMeasurementEntity e) {
        if (e == null) {
            return null;
        }

        return new QuantityMeasurementDTO(
                e.getThisValue(),
                e.getThisUnit(),
                e.getThisMeasurementType(),
                e.getThatValue(),
                e.getThatUnit(),
                e.getThatMeasurementType(),
                e.getOperation(),
                e.getResultValue(),
                e.getResultUnit(),
                e.getResultMeasurementType(),
                e.getResultString(),
                e.getErrorMessage(),
                e.isError()
        );
    }
}
