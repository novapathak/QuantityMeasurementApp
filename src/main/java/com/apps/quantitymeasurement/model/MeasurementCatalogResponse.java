package com.apps.quantitymeasurement.model;

import java.util.List;

public record MeasurementCatalogResponse(
        boolean googleOAuthEnabled,
        List<MeasurementTypeMetadata> measurementTypes,
        List<OperationMetadata> operations
) {
}
