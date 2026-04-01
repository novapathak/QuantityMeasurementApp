package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.model.*;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@Tag(name = "Quantity Measurements", description = "REST API for quantity measurement operations")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(
        value = "/api/v1/quantities",
        produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}
)
public class QuantityMeasurementController {

    private final IQuantityMeasurementService service;

    @Autowired
    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @Operation(summary = "Add two quantities")
    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO add(
            @Valid @RequestBody QuantityInputDTO input,
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.ADD);
        return service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Subtract one quantity from another")
    @PostMapping(value = "/subtract", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO subtract(
            @Valid @RequestBody QuantityInputDTO input,
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.SUBTRACT);
        return service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Multiply two quantities")
    @PostMapping(value = "/multiply", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO multiply(
            @Valid @RequestBody QuantityInputDTO input,
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.MULTIPLY);
        return service.multiply(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Divide one quantity by another")
    @PostMapping(value = "/divide", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO divide(@Valid @RequestBody QuantityInputDTO input) {
        requireThatQuantity(input, OperationType.DIVIDE);
        return service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @Operation(summary = "Compare two quantities")
    @PostMapping(value = "/compare", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO compare(@Valid @RequestBody QuantityInputDTO input) {
        requireThatQuantity(input, OperationType.COMPARE);
        return service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @Operation(summary = "Convert a quantity to a target unit")
    @PostMapping(value = "/convert", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO convert(
            @Valid @RequestBody QuantityInputDTO input,
            @RequestParam(required = false) String targetUnit
    ) {
        if (input.getThatQuantityDTO() == null && (targetUnit == null || targetUnit.isBlank())) {
            throw new QuantityMeasurementException("thatQuantityDTO or targetUnit is required for convert operation");
        }
        return service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Get operation history by operation type")
    @GetMapping({"/history/{operation}", "/history/operation/{operation}"})
    public List<QuantityMeasurementDTO> history(@PathVariable String operation) {
        return service.getHistoryByOperation(operation);
    }

    @Operation(summary = "Get operation history by measurement type")
    @GetMapping("/history/type/{measurementType}")
    public List<QuantityMeasurementDTO> historyByMeasurementType(@PathVariable String measurementType) {
        return service.getHistoryByMeasurementType(measurementType);
    }

    @Operation(summary = "Get error history")
    @GetMapping("/history/errored")
    public List<QuantityMeasurementDTO> erroredHistory() {
        return service.getErroredHistory();
    }

    @Operation(summary = "Get successful operation count by operation type")
    @GetMapping("/count/{operation}")
    public long count(@PathVariable String operation) {
        return service.countByOperation(operation);
    }

    private void requireThatQuantity(QuantityInputDTO input, OperationType operationType) {
        if (input.getThatQuantityDTO() == null) {
            throw new QuantityMeasurementException("thatQuantityDTO is required for " + operationType.name().toLowerCase() + " operation");
        }
    }
}
