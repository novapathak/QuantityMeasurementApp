package com.apps.quantitymeasurement;



public class Feet {

		
		private final double value;
		
		public Feet(double num) {
			this.value = num;
		}
		
		@Override
		public boolean equals(Object obj) {
			if(this == obj) {
				return true;
			}
			if(obj == null || getClass()!=obj.getClass() ) {
				return false;
			}
			Feet other = (Feet) obj;
			return Double.compare(this.value, other.value)==0;
			
		}
		
		
	}


