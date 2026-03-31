package com.apps.quantitymeasurement.model;

import lombok.Data;

@Data
public class QuantityInputDTO {
    private QuantityDTO thisQuantityDTO;
    private QuantityDTO thatQuantityDTO;
}