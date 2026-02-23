package com.apps.quantitymeasurement;

public class Length {
	
	private double value;
	private LengthUnit unit;
	
	private static final double EPSILON = 1e-6;
	
	public Length(double value, LengthUnit unit) {
		if (unit == null)
			throw new IllegalArgumentException("Unit cannot be null");

		this.value = value;
		this.unit = unit;
	}
	
	private double convertToBaseUnit(){
		
		return value*unit.getConversionFactor();	
	}
	
	public boolean compare(Length other) {
		if(other==null) {
			return false;
		}
		return Double.compare(this.convertToBaseUnit(), other.convertToBaseUnit())==0;
		
	}


		public Length add(Length other, LengthUnit targetUnit) {

			// Checking null operand
		    if (other == null) {
		        throw new IllegalArgumentException("Second operand cannot be null");
		    }

		    // Checking target unit 
		    if (targetUnit == null) {
		        throw new IllegalArgumentException("Target unit cannot be null");
		    }

		    // Checking Finite value 
		    if (!Double.isFinite(this.value) || !Double.isFinite(other.value)) {
		        throw new IllegalArgumentException("Values must be finite");
		    }

		    // Convert both operands to base unit (inches)
		    double baseValue1 = this.convertToBaseUnit();
		    double baseValue2 = other.convertToBaseUnit();

		    double sumBase = baseValue1 + baseValue2;

		    // Convert result to explicit target unit
		    double resultValue = sumBase / targetUnit.getConversionFactor();

		    return new Length(resultValue, targetUnit);
		}

		public static double convert(double value, LengthUnit source, LengthUnit target) {

			// Validation
			if (source == null || target == null) {
				throw new IllegalArgumentException("Unit cannot be null");
			}

			if (!Double.isFinite(value)) {
				throw new IllegalArgumentException("Value must be finite");
			}

			// Normalize to base unit
			double baseValue = value * source.getConversionFactor();

			// Convert to target unit
			return baseValue / target.getConversionFactor();
		}
		public Length convertTo(LengthUnit target) {

			double convertedValue = convert(this.value, this.unit, target);

			return new Length(convertedValue, target);
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
		double thisBase = this.value * this.unit.getConversionFactor();
		double otherBase = other.value * other.unit.getConversionFactor();

		return Math.abs(thisBase - otherBase) < EPSILON; 
	}
	

	

	@Override
	public String toString() {
		return "Quantity(" +value +", "+unit + ")";
	}
	public double getValue() {
		return value;
	}

	public LengthUnit getUnit() {
		return unit;
	}

}
