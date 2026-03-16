package com.apps.quantitymeasurement.units;

import java.util.function.Function;

import com.apps.quantitymeasurement.core.IMeasurable;
import com.apps.quantitymeasurement.core.SupportsArithmetic;

public enum TemperatureUnit implements IMeasurable {

	
	CELSIUS(value -> value, value -> value, "CELSIUS"),

	FAHRENHEIT(value -> (value - 32) * 5 / 9, value -> (value * 9 / 5) + 32, "FAHRENHEIT"),

	KELVIN(value -> value - 273.15, value -> value + 273.15, "KELVIN");

	private final Function<Double, Double> toBaseFunction;
	private final Function<Double, Double> fromBaseFunction;
	private final String unitName;

	// Temperature does NOT support arithmetic
	private final SupportsArithmetic supportsArithmetic = () -> false;

	TemperatureUnit(Function<Double, Double> toBaseFunction, Function<Double, Double> fromBaseFunction,
			String unitName) {
		this.toBaseFunction = toBaseFunction;
		this.fromBaseFunction = fromBaseFunction;
		this.unitName = unitName;
	}

	@Override
	public double convertToBaseUnit(double value) {
		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Value must be finite.");
		return toBaseFunction.apply(value);
	}

	@Override
	public double convertFromBaseUnit(double baseValue) {
		if (!Double.isFinite(baseValue))
			throw new IllegalArgumentException("Value must be finite.");
		return fromBaseFunction.apply(baseValue);
	}

	@Override 
	public String getUnitName() {
		return unitName;
	}

	// UC14: Temperature does NOT support arithmetic
	@Override
	public boolean supportsArithmetic() {
		return supportsArithmetic.isSupported();
	}

	@Override
	public void validateOperationSupport(String operation) {
		throw new UnsupportedOperationException("Temperature does not support " + operation + " operation.");
	}

	@Override
	public double getConversionFactor() {
		throw new UnsupportedOperationException("Temperature does not use a constant conversion factor.");
	}
	
	public String getMeasurementType() {
		return this.getClass().getName();
	}
	
	public IMeasurable getUnitInstance(String unitName) {
		for(TemperatureUnit unit : TemperatureUnit.values()) {
			if(unit.getUnitName().equalsIgnoreCase(unitName)) {
				return unit;
			}
		};
		throw new IllegalArgumentException("Invalid length unit: "+ unitName);	}
	
	
}