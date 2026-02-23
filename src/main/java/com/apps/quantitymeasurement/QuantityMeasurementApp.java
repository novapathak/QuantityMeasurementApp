package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
	
	public static void main(String[]args) {
		Scanner sc = new Scanner(System.in);
		
		
		// Cross-unit comparison
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCH);
		
		System.out.println("Input: "+ l1+ " and "+ l2 );
		System.out.println("Output: Equal "+ l1.equals(l2));
		
		
		// Same-unit comparison
		Length l3 = new Length(1.0, LengthUnit.INCH);
		Length l4 = new Length(12.0, LengthUnit.INCH);
		
		// Results of comparison
        System.out.println("Input values: " + l3 + " and " + l4);
        System.out.println("Output: Equal " + l3.equals(l4) );
		
    sc.close();	
      
    }
}
