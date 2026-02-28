package com.apps.quantitymeasurement;

public class QuantityMeasurementApp {

	public static <U extends IMeasurable> boolean demonstrateEquality(Quantity<U> q1, Quantity<U> q2) {
		return q1.equals(q2);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateConversion(Quantity<U> quantity, U targetUnit) {
		return quantity.convertTo(targetUnit);
	}

	public static <U extends IMeasurable> Quantity<U> demonstrateAddition(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {
		return q1.add(q2, targetUnit);
	}

	// Subtraction
	public static <U extends IMeasurable> Quantity<U> demonstrateSubtraction(Quantity<U> q1, Quantity<U> q2,
			U targetUnit) {
		return q1.subtract(q2, targetUnit);
	}

	// Division
	public static <U extends IMeasurable> double demonstrateDivision(Quantity<U> q1, Quantity<U> q2) {
		return q1.divide(q2);
	}

	public static void main(String[] args) {

		// LENGTH
		Quantity<LengthUnit> length1 = new Quantity<>(1.0, LengthUnit.FEET);
		Quantity<LengthUnit> length2 = new Quantity<>(12.0, LengthUnit.INCH);
		System.out.println("Length Equality: " + demonstrateEquality(length1, length2));
		System.out.println("Length Conversion: " + demonstrateConversion(length1, LengthUnit.INCH));
		System.out.println("Length Addition: " + demonstrateAddition(length1, length2, LengthUnit.FEET));
		System.out.println("Length Subtraction: " + demonstrateSubtraction(length1, length2, LengthUnit.FEET));
		System.out.println("Length Division: " + demonstrateDivision(length1, length2));

		// WEIGHT
		Quantity<WeightUnit> weight1 = new Quantity<>(1.0, WeightUnit.KILOGRAM);
		Quantity<WeightUnit> weight2 = new Quantity<>(1000.0, WeightUnit.GRAM);
		System.out.println("\nWeight Equality: " + demonstrateEquality(weight1, weight2));
		System.out.println("Weight conversion: " + demonstrateConversion(weight1, WeightUnit.GRAM));
		System.out.println("Weight Addition: " + demonstrateAddition(weight1, weight2, WeightUnit.KILOGRAM));
		System.out.println("Weight Subtraction: " + demonstrateSubtraction(weight1, weight2, WeightUnit.KILOGRAM));
		System.out.println("Weight Division: " + demonstrateDivision(weight1, weight2));

		// VOLUME
		Quantity<VolumeUnit> volume1 = new Quantity<>(1.0, VolumeUnit.LITRE);
		Quantity<VolumeUnit> volume2 = new Quantity<>(25.0, VolumeUnit.GALLON);
		System.out.println("\nVolume Equality: " + demonstrateEquality(volume1, volume2));
		System.out.println("Volume conversion: " + demonstrateConversion(volume1, VolumeUnit.MILLILITRE));
		System.out.println("Volume Subtraction: " + demonstrateSubtraction(volume1, volume2, VolumeUnit.LITRE));
		System.out.println("Volume Division: " + demonstrateDivision(volume1, volume2));

		// TEMPERATURE (UC14)
		// =========================
		System.out.println("\n===== TEMPERATURE =====");

		Quantity<TemperatureUnit> temp1 = new Quantity<>(0.0, TemperatureUnit.CELSIUS);
		Quantity<TemperatureUnit> temp2 = new Quantity<>(32.0, TemperatureUnit.FAHRENHEIT);

		System.out.println("Temperature Equality (0C == 32F): " + demonstrateEquality(temp1, temp2));

		System.out.println("Convert 100C to F: "
				+ demonstrateConversion(new Quantity<>(100.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT));

		System.out.println("Convert 273.15K to C: "
				+ demonstrateConversion(new Quantity<>(273.15, TemperatureUnit.KELVIN), TemperatureUnit.CELSIUS));

		System.out.println("Convert -40C to F: "
				+ demonstrateConversion(new Quantity<>(-40.0, TemperatureUnit.CELSIUS), TemperatureUnit.FAHRENHEIT));

		// Unsupported Operations Demo
		try {
			demonstrateAddition(temp1, temp2, TemperatureUnit.CELSIUS);
		} catch (UnsupportedOperationException e) {
			System.out.println("Addition Error: " + e.getMessage());
		}

		try {
			demonstrateSubtraction(temp1, temp2, TemperatureUnit.CELSIUS);
		} catch (UnsupportedOperationException e) {
			System.out.println("Subtraction Error: " + e.getMessage());
		}

		try {
			demonstrateDivision(temp1, temp2);
		} catch (UnsupportedOperationException e) {
			System.out.println("Division Error: " + e.getMessage());
		}

	}
}