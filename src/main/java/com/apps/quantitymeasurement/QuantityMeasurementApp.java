package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp {
	public static boolean demonstrateLengthEquality(Length l1, Length l2) {
		return l1.equals(l2);
	}

	public static double demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {
		return Length.convert(value, fromUnit, toUnit);
	}

	public static Length demonstrateLengthConversion(Length length, LengthUnit toUnit) {
		return length.convertTo(toUnit);
	}

	public static void main(String[] args) {

//		Length length1 = new Length(1.0, LengthUnit.FEET);
//		Length length2 = new Length(12.0, LengthUnit.INCHES);
//
//		System.out.println("Input: " + length1 + " and " + length2);
//		System.out.println("Equal (" + demonstrateLengthEquality(length1, length2) + ")");
//
//		Length length3 = new Length(1.0, LengthUnit.INCHES);
//		Length length4 = new Length(1.0, LengthUnit.INCHES);
//
//		System.out.println("Input: " + length3 + " and " + length4);
//		System.out.println("Equal (" + demonstrateLengthEquality(length3, length4) + ")");
//
//		Length length5 = new Length(100.0, LengthUnit.CENTIMETERS);
//		Length length6 = new Length(39.3701, LengthUnit.INCHES);
//
//		System.out.println("Input: " + length5 + " and " + length6);
//		System.out.println("Equal (" + demonstrateLengthEquality(length5, length6) + ")");

		System.out.println(
				"convert(1.0, FEET, INCHES) = " + demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH));
		System.out.println(
				"convert(3.0, YARDS, FEET) = " + demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET));
		System.out.println("convert(36.0, INCHES, YARDS) = "
				+ demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS));
		System.out.println("convert(1.0, CENTIMETERS, INCHES) = "
				+ demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH));
		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length feet = new Length(3.0, LengthUnit.FEET);
		System.out.println("Equality check: " + demonstrateLengthEquality(yard, feet));
	}
}