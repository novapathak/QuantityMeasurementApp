package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;

public record OperationMetadata(
        @Schema(example = "ADD")
        String name,
        @Schema(example = "Add")
        String label,
        @Schema(description = "Whether this operation needs thatQuantityDTO")
        boolean requiresSecondQuantity,
        @Schema(description = "Whether targetUnit query parameter can be provided")
        boolean allowsTargetUnit,
        @Schema(description = "Whether targetUnit query parameter is mandatory")
        boolean requiresTargetUnit
) {
}
