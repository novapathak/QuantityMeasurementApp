package com.apps.quantitymeasurement;

import java.util.Objects;

public class Quantity<U extends IMeasurable> {
	private final double value;
	private final U unit;

	public static final double EPSILON = 1e-5;

	// Constructor
	public Quantity(double value, U unit) {

		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null.");

		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Value must be finite.");

		this.value = value;
		this.unit = unit;
	}

	// Convert to base unit
	private double toBaseUnit() {
		return unit.convertToBaseUnit(value);
	}

	// EQUALITY

	@Override
	public boolean equals(Object obj) {

		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		Quantity<?> other = (Quantity<?>) obj;
		// Prevent cross-category comparison
		if (this.unit.getClass() != other.unit.getClass())
			return false;
		double thisBase = this.toBaseUnit();
		double otherBase = other.unit.convertToBaseUnit(other.value);
		return Math.abs(thisBase - otherBase) < EPSILON;
	}

	@Override
	public int hashCode() {
		return Objects.hash(toBaseUnit());
	}

	// CONVERSION

	public Quantity<U> convertTo(U targetUnit) {
		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null.");
		double baseValue = this.toBaseUnit();
		double convertedValue = targetUnit.convertFromBaseUnit(baseValue);
		return new Quantity<>(convertedValue, targetUnit);
	}

	// ADDITION

	// Implicit target (first operand unit)
	public Quantity<U> add(Quantity<U> other) {
		return add(other, this.unit);
	}

	// Explicit target unit
	public Quantity<U> add(Quantity<U> other, U targetUnit) {
		validateArithmeticOperands(other, targetUnit, true);
		double baseResult = performBaseArithmetic(other, ArithmeticOperation.ADD);
		double resultValue = targetUnit.convertFromBaseUnit(baseResult);
		return new Quantity<>(roundToTwoDecimal(resultValue), targetUnit);
	}

	// ARITHMATIC OPERATIONS

	// SUBTRACTION
	public Quantity<U> subtract(Quantity<U> other) {
		return subtract(other, this.unit);
	}

	// SUBTRACTION BY TARGETED UNIT
	public Quantity<U> subtract(Quantity<U> other, U targetUnit) {
		validateArithmeticOperands(other, targetUnit, true);
		double baseResult = performBaseArithmetic(other, ArithmeticOperation.SUBTRACT);
		double converted = targetUnit.convertFromBaseUnit(baseResult);
		return new Quantity<>(roundToTwoDecimal(converted), targetUnit);
	}

	// DIVIDE
	public double divide(Quantity<U> other) {
		validateArithmeticOperands(other, null, false);
		return performBaseArithmetic(other, ArithmeticOperation.DIVIDE);
	}

	private double performBaseArithmetic(Quantity<U> other, ArithmeticOperation operation) {

		this.unit.validateOperationSupport(operation.name());
		double baseThis = this.toBaseUnit();
		double baseOther = other.toBaseUnit();
		return operation.compute(baseThis, baseOther);
	}

	// METHOD TO VALIDATE OPERAND
	private void validateArithmeticOperands(Quantity<U> other, U targetUnit, boolean targetUnitRequired) {

		if (other == null)
			throw new IllegalArgumentException("Quantity cannot be null");

		if (this.unit.getClass() != other.unit.getClass())
			throw new IllegalArgumentException("Cross-category arithmetic not allowed");

		if (!Double.isFinite(this.value) || !Double.isFinite(other.value))
			throw new IllegalArgumentException("Values must be finite numbers");

		if (targetUnitRequired && targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null");
	}

	// ROUND TO DECIMAL
	private double roundToTwoDecimal(double value) {
		return Math.round(value * 100.0) / 100.0;
	}
	// GETTERS

	public double getValue() {
		return value;
	}

	public U getUnit() {
		return unit;
	}

	@Override
	public String toString() {
		return "Quantity(" + value + ", " + unit.getUnitName() + ")";
	}
}