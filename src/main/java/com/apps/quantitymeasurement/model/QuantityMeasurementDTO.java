package com.apps.quantitymeasurement.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementDTO {

    private Double thisValue;
    private String thisUnit;
    private String thisMeasurementType;

    private Double thatValue;
    private String thatUnit;
    private String thatMeasurementType;

    private String operation;

    private Double resultValue;
    private String resultUnit;
    private String resultMeasurementType;

    private String resultString;

    private String errorMessage;
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
