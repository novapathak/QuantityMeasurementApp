package com.apps.quantitymeasurement;

public interface IMeasurable {

	
	double convertFromBaseUnit(double baseValue);
	double convertToBaseUnit(double value);
	double getConversionFactor();
	

	String getUnitName();
}