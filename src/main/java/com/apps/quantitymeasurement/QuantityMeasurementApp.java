package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	// Common display utility method
	private static void displayResult(String operation, Object result) {
		System.out.println(operation + " : " + result);
	}

	// Static conversion demonstration
	public static double demonstrateLengthConversion(double value, LengthUnit from, LengthUnit to) {

		double result = Length.convert(value, from, to);

		displayResult("convert(" + value + ", " + from + ", " + to + ")", result);

		return result;
	}

	// Instance conversion demonstration (Overloaded method)
	public static Length demonstrateLengthConversion(Length length, LengthUnit to) {

		if (length == null) {
			throw new IllegalArgumentException("Length object cannot be null");
		}

		Length result = length.convertTo(to);

		displayResult(length + " converted to " + to, result);

		return result;
	}

	public static void main(String[] args) {

		// Conversion demonstrations
		demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);
		demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET);
		demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS);
		demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH);

		// Object-based conversion demo
		Length length = new Length(2.0, LengthUnit.YARDS);
		demonstrateLengthConversion(length, LengthUnit.FEET);

		
	}
}