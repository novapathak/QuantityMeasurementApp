package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.model.*;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quantities")
public class QuantityMeasurementController {

    @Autowired
    private IQuantityMeasurementService service;

    @PostMapping("/add")
    public QuantityMeasurementDTO add(@RequestBody QuantityInputDTO input) {
        return service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/subtract")
    public QuantityMeasurementDTO subtract(@RequestBody QuantityInputDTO input) {
        return service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/divide")
    public QuantityMeasurementDTO divide(@RequestBody QuantityInputDTO input) {
        return service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/compare")
    public QuantityMeasurementDTO compare(@RequestBody QuantityInputDTO input) {
        return service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @PostMapping("/convert")
    public QuantityMeasurementDTO convert(@RequestBody QuantityInputDTO input) {
        return service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @GetMapping("/history/{operation}")
    public List<QuantityMeasurementDTO> history(@PathVariable String operation) {
        return service.getHistoryByOperation(operation);
    }

    @GetMapping("/count/{operation}")
    public long count(@PathVariable String operation) {
        return service.countByOperation(operation);
    }
}