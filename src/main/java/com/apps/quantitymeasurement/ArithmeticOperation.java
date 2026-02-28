package com.apps.quantitymeasurement;

import java.util.function.DoubleBinaryOperator;

public enum ArithmeticOperation {
	ADD((a, b) -> a + b), 
	SUBTRACT((a, b) -> a - b), 
	DIVIDE((a, b) -> {
		if (Math.abs(b) < Quantity.EPSILON) {
			throw new ArithmeticException("Cannot divide by zero quantity");
		}
		return a / b;
	});

	private final DoubleBinaryOperator operator;

	ArithmeticOperation(DoubleBinaryOperator operator) {
		this.operator = operator;
	}

	public double compute(double left, double right) {
		return operator.applyAsDouble(left, right);
	}
}