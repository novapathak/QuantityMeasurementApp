package com.apps.quantitymeasurement.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiErrorResponse {
    @Schema(description = "Error timestamp")
    private LocalDateTime timestamp;
    @Schema(example = "400")
    private int status;
    @Schema(example = "Quantity Measurement Error")
    private String error;
    @Schema(example = "Unit must be valid for the specified measurement type")
    private String message;
    @Schema(example = "/api/v1/quantities/compare")
    private String path;
}
