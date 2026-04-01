package com.apps.quantitymeasurement.model;
public enum OperationType {
    ADD, SUBTRACT, MULTIPLY, DIVIDE, COMPARE, CONVERT;

    public static OperationType fromValue(String value) {
        for (OperationType operationType : values()) {
            if (operationType.name().equalsIgnoreCase(value)) {
                return operationType;
            }
        }
        throw new IllegalArgumentException("Unsupported operation: " + value);
    }
}
