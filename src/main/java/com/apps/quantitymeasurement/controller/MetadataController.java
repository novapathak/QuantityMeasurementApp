package com.apps.quantitymeasurement.controller;

import com.apps.quantitymeasurement.config.GoogleOAuth2Properties;
import com.apps.quantitymeasurement.exception.ApiErrorResponse;
import com.apps.quantitymeasurement.model.MeasurementCatalogResponse;
import com.apps.quantitymeasurement.model.MeasurementTypeMetadata;
import com.apps.quantitymeasurement.model.OperationMetadata;
import com.apps.quantitymeasurement.util.MeasurementUnitRegistry;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/metadata")
@Tag(name = "Metadata", description = "API metadata and supported units")
public class MetadataController {

    private final GoogleOAuth2Properties googleOAuth2Properties;

    @Operation(summary = "Get supported units and operation capabilities")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Metadata fetched", content = @Content(schema = @Schema(implementation = MeasurementCatalogResponse.class))),
            @ApiResponse(responseCode = "500", description = "Unexpected error", content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
    })
    @GetMapping("/measurements")
    public MeasurementCatalogResponse measurements() {
        Map<String, List<String>> unitsByMeasurementType = MeasurementUnitRegistry.supportedUnitsByMeasurementType();

        List<MeasurementTypeMetadata> measurementTypes = unitsByMeasurementType.entrySet().stream()
                .map(entry -> new MeasurementTypeMetadata(
                        entry.getKey(),
                        labelForMeasurementType(entry.getKey()),
                        MeasurementUnitRegistry.supportsArithmeticForMeasurementType(entry.getKey()),
                        entry.getValue()
                ))
                .toList();

        List<OperationMetadata> operations = List.of(
                new OperationMetadata("ADD", "Add", true, true, false),
                new OperationMetadata("SUBTRACT", "Subtract", true, true, false),
                new OperationMetadata("MULTIPLY", "Multiply", true, true, false),
                new OperationMetadata("DIVIDE", "Divide", true, false, false),
                new OperationMetadata("COMPARE", "Compare", true, false, false),
                new OperationMetadata("CONVERT", "Convert", false, true, true)
        );

        return new MeasurementCatalogResponse(
                googleOAuth2Properties.isEnabled(),
                measurementTypes,
                operations
        );
    }

    private String labelForMeasurementType(String measurementType) {
        return measurementType.replace("Unit", "");
    }
}
