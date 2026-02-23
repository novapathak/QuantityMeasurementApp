package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
	
	public static void demonstrateLengthComparison(double value1, LengthUnit unit1, double value2, LengthUnit unit2) {

		Length l1 = new Length(value1, unit1);
		Length l2 = new Length(value2, unit2);

		boolean result = l1.equals(l2);

		System.out.println("Length 1: " + l1);
		System.out.println("Length 2: " + l2);
		System.out.println("Are Both length equal? " + result);
	}
	public static void main(String[]args) {
		Scanner sc = new Scanner(System.in);
		
		
		// Feet and Inches
				demonstrateLengthComparison(1.0, LengthUnit.FEET, 12.0, LengthUnit.INCH);

				// Yards and Inches
				demonstrateLengthComparison(1.0, LengthUnit.YARDS, 36.0, LengthUnit.INCH);

				// Centimeters and Inches
				demonstrateLengthComparison(100.0, LengthUnit.CENTIMETERS, 39.3701, LengthUnit.INCH);

				// Feet and Yards
				demonstrateLengthComparison(3.0, LengthUnit.FEET, 1.0, LengthUnit.YARDS);

				// Centimeters and Feet
				demonstrateLengthComparison(30.48, LengthUnit.CENTIMETERS, 1.0, LengthUnit.FEET);
		
    sc.close();	
      
    }
}
