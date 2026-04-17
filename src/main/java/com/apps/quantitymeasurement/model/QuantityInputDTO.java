package com.apps.quantitymeasurement.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityInputDTO {

    @Valid
    @NotNull(message = "thisQuantityDTO is required")
    @Schema(description = "Primary quantity used by all operations")
    private QuantityDTO thisQuantityDTO;

    @Valid
    @Schema(description = "Secondary quantity used by add/subtract/multiply/divide/compare", nullable = true)
    private QuantityDTO thatQuantityDTO;
}
