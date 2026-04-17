package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.exception.ApiErrorResponse;
import com.apps.quantitymeasurement.exception.QuantityMeasurementException;
import com.apps.quantitymeasurement.model.OperationType;
import com.apps.quantitymeasurement.model.QuantityInputDTO;
import com.apps.quantitymeasurement.model.QuantityMeasurementDTO;
import com.apps.quantitymeasurement.service.IQuantityMeasurementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

    public QuantityMeasurementController(IQuantityMeasurementService service) {
        this.service = service;
    }

    @Operation(summary = "Add two quantities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Addition result", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/add", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO add(
            @Valid @RequestBody QuantityInputDTO input,
            @Parameter(description = "Optional output unit for the result", example = "FEET")
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.ADD);
        return service.add(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Subtract one quantity from another")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Subtraction result", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/subtract", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO subtract(
            @Valid @RequestBody QuantityInputDTO input,
            @Parameter(description = "Optional output unit for the result", example = "INCH")
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.SUBTRACT);
        return service.subtract(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Multiply two quantities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Multiplication result", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/multiply", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO multiply(
            @Valid @RequestBody QuantityInputDTO input,
            @Parameter(description = "Optional output unit for the result", example = "YARDS")
            @RequestParam(required = false) String targetUnit
    ) {
        requireThatQuantity(input, OperationType.MULTIPLY);
        return service.multiply(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Divide one quantity by another")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Division ratio", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/divide", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO divide(@Valid @RequestBody QuantityInputDTO input) {
        requireThatQuantity(input, OperationType.DIVIDE);
        return service.divide(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @Operation(summary = "Compare two quantities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comparison result", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/compare", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO compare(@Valid @RequestBody QuantityInputDTO input) {
        requireThatQuantity(input, OperationType.COMPARE);
        return service.compare(input.getThisQuantityDTO(), input.getThatQuantityDTO());
    }

    @Operation(summary = "Convert a quantity to a target unit")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Converted quantity", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid request", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @PostMapping(value = "/convert", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public QuantityMeasurementDTO convert(
            @Valid @RequestBody QuantityInputDTO input,
            @Parameter(description = "Optional target unit. If omitted, thatQuantityDTO.unit can be used.", example = "INCH")
            @RequestParam(required = false) String targetUnit
    ) {
        if (input.getThatQuantityDTO() == null && (targetUnit == null || targetUnit.isBlank())) {
            throw new QuantityMeasurementException("thatQuantityDTO or targetUnit is required for convert operation");
        }
        return service.convert(input.getThisQuantityDTO(), input.getThatQuantityDTO(), targetUnit);
    }

    @Operation(summary = "Get operation history by operation type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History entries", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid operation type", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping({"/history/{operation}", "/history/operation/{operation}"})
    public List<QuantityMeasurementDTO> history(@PathVariable String operation) {
        return service.getHistoryByOperation(operation);
    }

    @Operation(summary = "Get operation history by measurement type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "History entries", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "400", description = "Invalid measurement type", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/history/type/{measurementType}")
    public List<QuantityMeasurementDTO> historyByMeasurementType(@PathVariable String measurementType) {
        return service.getHistoryByMeasurementType(measurementType);
    }

    @Operation(summary = "Get error history")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Errored history entries", content = @Content(schema = @Schema(implementation = QuantityMeasurementDTO.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/history/errored")
    public List<QuantityMeasurementDTO> erroredHistory() {
        return service.getErroredHistory();
    }

    @Operation(summary = "Get successful operation count by operation type")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Count returned"),
            @ApiResponse(responseCode = "400", description = "Invalid operation type", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
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
