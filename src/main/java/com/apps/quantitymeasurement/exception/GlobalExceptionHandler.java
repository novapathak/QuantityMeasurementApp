package com.apps.quantitymeasurement.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = fieldError != null ? fieldError.getDefaultMessage() : "Validation failed";
        String path = fieldError != null ? ex.getBindingResult().getObjectName() : request.getRequestURI();

        return buildResponse(HttpStatus.BAD_REQUEST, "Quantity Measurement Error", message, path);
    }

    @ExceptionHandler(QuantityMeasurementException.class)
    public ResponseEntity<ApiErrorResponse> handleQuantityException(
            QuantityMeasurementException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.BAD_REQUEST, "Quantity Measurement Error", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(ArithmeticException.class)
    public ResponseEntity<ApiErrorResponse> handleArithmeticException(
            ArithmeticException ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(
            Exception ex,
            HttpServletRequest request
    ) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error", ex.getMessage(), request.getRequestURI());
    }

    private ResponseEntity<ApiErrorResponse> buildResponse(HttpStatus status, String error, String message, String path) {
        ApiErrorResponse response = new ApiErrorResponse(LocalDateTime.now(), status.value(), error, message, path);
        return ResponseEntity.status(status).body(response);
    }
}
