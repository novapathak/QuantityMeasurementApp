package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record MeasurementCatalogResponse(
        @Schema(description = "Whether Google OAuth2 login is enabled")
        boolean googleOAuthEnabled,
        @Schema(description = "Supported measurement types and their units")
        List<MeasurementTypeMetadata> measurementTypes,
        @Schema(description = "Supported operations and their input requirements")
        List<OperationMetadata> operations
) {
}
