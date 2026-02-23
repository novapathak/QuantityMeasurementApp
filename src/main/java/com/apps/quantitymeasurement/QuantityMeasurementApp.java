package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
	
	// Static equality demonstration
		public static boolean demonstrateLengthEquality(Length l1, Length l2) {
			return l1.equals(l2);
		}

		// Private helper for printing equality
		private static void checkEquality(Length l1, Length l2) {

			System.out.println(l1 + " and " + l2 + " Equal: " + demonstrateLengthEquality(l1, l2));
		}


		// Static method for length conversion 
		public static void demonstrateLengthConversion(double value, LengthUnit fromUnit, LengthUnit toUnit) {

			double result = LengthUnit.valueOf(fromUnit.name()).convertFromBaseUnit(fromUnit.convertToBaseUnit(value));

			System.out.println(value + " " + fromUnit + " is " + result + " " + toUnit);
		}

		// Object-based conversion
		public static void demonstrateLengthConversion(Length length, LengthUnit toUnit) {

			System.out.println(length + " converted to " + toUnit + " is " + length.convertTo(toUnit));
		}

		// Static method for length addition 
		public static void demonstrateLengthAddition(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {

			Length length1 = new Length(value1, unit1);
			Length length2 = new Length(value2, unit2);

			Length result = length1.add(length2, LengthUnit.FEET);

			System.out.println(length1 + " + " + length2 + " = " + result);
		}

		// Static method for length addition with target type
		public static void demonstrateLengthAddition(double value1, LengthUnit unit1, double value2, LengthUnit unit2,
				LengthUnit targetUnit) {

			Length length1 = new Length(value1, unit1);
			Length length2 = new Length(value2, unit2);

			Length result = length1.add(length2, targetUnit);

			System.out.println(length1 + " + " + length2 + " = " + result);
		}

		public static void main(String[] args) {

			// Equality method 
			checkEquality(new Length(1.0, LengthUnit.FEET), new Length(12.0, LengthUnit.INCH));

			// Conversion method 
			demonstrateLengthConversion(1.0, LengthUnit.FEET, LengthUnit.INCH);

			demonstrateLengthConversion(new Length(2.0, LengthUnit.YARDS), LengthUnit.FEET);

			// Addition
			demonstrateLengthAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);

			// Addition with target unit
			demonstrateLengthAddition(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH, LengthUnit.YARDS);
		}
	}