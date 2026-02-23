package com.apps.quantitymeasurement;

public class Length {
	
	private double value;
	private LengthUnit unit;
	
	public Length(double value, LengthUnit unit) {
		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		this.value = value;
		this.unit = unit;
	}
	
	private double convertToBaseUnit(){
		
		return value*unit.getConversionFactorToFeet();	
	}
	
	public boolean compare(Length other) {
		if(other==null) {
			return false;
		}
		return Double.compare(this.convertToBaseUnit(), other.convertToBaseUnit())==0;
		
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		if(obj==null || getClass() != obj.getClass()) {
			return false;
		}
		Length other = (Length)obj;
		return this.compare(other);
	}
	public Length convertTo(LengthUnit targetUnit) {
		if (targetUnit == null)
			throw new IllegalArgumentException("Target unit cannot be null.");
		double convertedValue = convert(this.value, this.unit, targetUnit);
		return new Length(convertedValue, targetUnit);
	}

	public static double convert(double value, LengthUnit source, LengthUnit target) {

		if (!Double.isFinite(value))
			throw new IllegalArgumentException("Value must be finite.");

		if (source == null || target == null)
			throw new IllegalArgumentException("Units cannot be null.");
		double valueInBase = value * source.getConversionFactorToFeet();
		return valueInBase / target.getConversionFactorToFeet();
	}

	@Override
	public String toString() {
		return "Quantity(" +value +", "+unit + ")";
	}


}
