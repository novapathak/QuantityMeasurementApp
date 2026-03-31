package com.apps.quantitymeasurement.model;

import lombok.Data;

@Data
public class QuantityDTO {
    private Double value;
    private String unit;
    private String measurementType;
}