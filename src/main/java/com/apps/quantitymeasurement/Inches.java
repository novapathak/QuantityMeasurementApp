package com.apps.quantitymeasurement;

public class Inches {
	
	private final double value;
	
	public Inches(double num) {
		this.value = num;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(this==obj) {
			return true;
		}
		if(obj == null || getClass()!= obj.getClass() ) {
			return false;
		}
		Inches other = (Inches)obj;
		return Double.compare(this.value, other.value)==0;
	}

}
