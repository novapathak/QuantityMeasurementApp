package com.apps.quantitymeasurement.core;

public interface IMeasurable {

	double getConversionFactor();

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();

	public String getMeasurementType();

	default boolean supportsArithmetic() {
		return true;
	}

	default void validateOperationSupport(String operation) {
	}
}