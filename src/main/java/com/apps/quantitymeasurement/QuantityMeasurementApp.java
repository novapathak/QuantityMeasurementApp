package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
	
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

		System.out.println("convert(1.0, FEET, INCH) = " + demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH));
		System.out.println(
				"convert(3.0, YARDS, FEET) = " + demonstrateLengthConversion(3.0, LengthUnit.YARDS, LengthUnit.FEET));
		System.out.println("convert(36.0, INCH, YARDS) = " + demonstrateLengthConversion(36.0, LengthUnit.INCH, LengthUnit.YARDS));
		System.out.println("convert(1.0, CENTIMETERS, INCH) = " + demonstrateLengthConversion(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCH));
		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length feet = new Length(3.0, LengthUnit.FEET);
		System.out.println("Equality check: " + demonstrateLengthEquality(yard, feet));
	}
}