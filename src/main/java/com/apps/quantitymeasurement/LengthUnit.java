package com.apps.quantitymeasurement;

 enum LengthUnit {
	 FEET(1.0),
	 INCH(1.0 / 12.0), YARDS(36.0), CENTIMETERS(0.393701);

	 
	 private final double conversionFactorToFeet;
	 
	 
	 LengthUnit(double conversionFactorToFeet){
		 this.conversionFactorToFeet = conversionFactorToFeet;
	 }
	 
	 public double getConversionFactorToFeet() {
		 return conversionFactorToFeet;
	 }

}
