package com.apps.quantitymeasurement.model;

public class QuantityDTO {

	    public double value;
	    public IMeasurableUnit unit;

	    public QuantityDTO(double value, IMeasurableUnit unit) {
	        this.value = value;
	        this.unit = unit;
	    
	
}
	public interface IMeasurableUnit{
		public String getUnitName();
		public String getMeasurementType();
	}
	
	public enum LengthUnit implements IMeasurableUnit{
		
		FEET,  YARDS, CENTIMETERS, INCH;
		
		@Override
		public String getUnitName() {
			return this.name();
		}
		
		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
		
	}
	
	enum WeigthUnit implements IMeasurableUnit{
		
		GRAM, KILOGRAM;
		@Override
		public String getUnitName() {
			return this.name();
		}
		
		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}
		
		
	}
	enum VolumeUnit implements IMeasurableUnit{
		 LITRE, MILLILITRE, GALLON;
		@Override
		public String getUnitName() {
			return this.name();
		}
		
		@Override
		public String getMeasurementType() {
			return this.getClass().getSimpleName();
		}

	}
	enum TemperatureUnit implements IMeasurableUnit{
		 CELSIUS, FAHRENHEIT, KELVIN;

        @Override
        public String getUnitName() {
            return this.name();
        }

        @Override
        public String getMeasurementType() {
            return this.getClass().getSimpleName();
        }
	
	}
	

}
