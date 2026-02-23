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



		Length feet = new Length(1.0, LengthUnit.FEET);
		Length inches = new Length(12.0, LengthUnit.INCH);

		Length result1 = feet.add(inches);
		System.out.println("1 FEET + 12 INCHES = " + result1);

		Length result2 = inches.add(feet);
		System.out.println("12 INCHES + 1 FEET = " + result2);

		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length result3 = yard.add(new Length(3.0, LengthUnit.FEET));
		System.out.println("1 YARD + 3 FEET = " + result3);

		Length cm = new Length(2.54, LengthUnit.CENTIMETERS);
		Length result4 = cm.add(new Length(1.0, LengthUnit.INCH));
		System.out.println("2.54 CM + 1 INCH = " + result4);
	}
}