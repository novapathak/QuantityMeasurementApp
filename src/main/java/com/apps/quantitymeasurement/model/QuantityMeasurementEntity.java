package com.apps.quantitymeasurement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "quantity_measurements",
        indexes = {
                @Index(name = "idx_quantity_measurements_operation", columnList = "operation"),
                @Index(name = "idx_quantity_measurements_measurement_type", columnList = "this_measurement_type"),
                @Index(name = "idx_quantity_measurements_created_at", columnList = "created_at"),
                @Index(name = "idx_quantity_measurements_is_error", columnList = "is_error")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QuantityMeasurementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "this_value", nullable = false)
    private Double thisValue;

    @Column(name = "this_unit", nullable = false, length = 50)
    private String thisUnit;

    @Column(name = "this_measurement_type", nullable = false, length = 50)
    private String thisMeasurementType;

    @Column(name = "that_value")
    private Double thatValue;

    @Column(name = "that_unit", length = 50)
    private String thatUnit;

    @Column(name = "that_measurement_type", length = 50)
    private String thatMeasurementType;

    @Column(nullable = false, length = 20)
    private String operation;

    @Column(name = "result_value")
    private Double resultValue;

    @Column(name = "result_unit", length = 50)
    private String resultUnit;

    @Column(name = "result_measurement_type", length = 50)
    private String resultMeasurementType;

    @Column(name = "result_string", length = 255)
    private String resultString;

    @Column(name = "error_message", length = 500)
    private String errorMessage;

    @Column(name = "is_error", nullable = false)
    private boolean isError;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        if (this.createdAt == null) {
            this.createdAt = now;
        }
        if (this.updatedAt == null) {
            this.updatedAt = now;
        }
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
