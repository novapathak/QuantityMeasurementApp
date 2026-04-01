package com.apps.quantitymeasurement.model;

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
    private QuantityDTO thisQuantityDTO;

    @Valid
    private QuantityDTO thatQuantityDTO;
}
