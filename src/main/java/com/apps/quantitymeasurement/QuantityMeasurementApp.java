package com.apps.quantitymeasurement;
import java.util.*;

/**
 * Hello world!
 *
 */
public class QuantityMeasurementApp 
{
	public static boolean compareFeet(double value1, double value2) {
		Feet f1 = new Feet(value1);
		Feet f2 = new Feet(value2);
		return f1.equals(f2);
	}
	public static boolean compareInches(double value1, double value2) {
		Feet i1 = new Feet(value1);
		Feet i2 = new Feet(value2);
		return i1.equals(i2);
	}
	
	
    public static void main( String[] args )
    {
    	Scanner sc = new Scanner(System.in);
    	try {
    	System.out.println("Enter first value in feet: ");
    	double feet1 = Double.parseDouble(sc.nextLine());
    	System.out.println("Enter second value in feet: ");
    	double feet2 = Double.parseDouble(sc.nextLine());
    	
    	System.out.println("Equal ? ( " + compareInches(feet1,feet2)+" )");
    	
    	System.out.println("Enter first value in Inches: ");
    	double Inch1 = Double.parseDouble(sc.nextLine());
    	System.out.println("Enter second value in Inches: ");
    	double Inch2 = Double.parseDouble(sc.nextLine());
    	
    	System.out.println("Equal ? ( " + compareInches(Inch1, Inch2)+ " )");
    	
    	}
    	catch (NumberFormatException e) {
    		System.out.println("Invalid input! Please enter numeric values only");
    	}
    	
    sc.close();	
      
    }
}
