package com.apps.quantitymeasurement.core;

public interface IMeasurable {

	double getConversionFactor();

	double convertToBaseUnit(double value);

	double convertFromBaseUnit(double baseValue);

	String getUnitName();
	SupportsArithmetic supportsArithmetic = () -> true;
	
	public String getMeasurementType();
	public IMeasurable getUnitInstance(String unitName);

	default boolean supportsArithmetic() {
		return supportsArithmetic.isSupported();
	}
		default void validateOperationSupport(String operation) {
	}
}