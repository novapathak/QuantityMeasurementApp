package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
		
    public static void main( String[] args )
    {
    	Scanner sc = new Scanner(System.in);
    	try {
    	System.out.println("Enter first value in feet: ");
    	double feet1 = Double.parseDouble(sc.nextLine());
    	System.out.println("Enter second value in feet: ");
    	double feet2 = Double.parseDouble(sc.nextLine());
    	
    	
    	Feet f1 = new Feet(feet1);
    	Feet f2 = new Feet(feet2);
    	
    	boolean result = f1.equals(f2);
    	System.out.println("Are the two values equal?   "+ result);
    	}
    	catch (NumberFormatException e) {
    		System.out.println("Invalid input! Please enter numeric values only");
    	}
    	
    sc.close();	
      
    }
}
