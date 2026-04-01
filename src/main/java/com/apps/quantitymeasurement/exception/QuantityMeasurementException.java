package com.apps.quantitymeasurement.exception;

public class QuantityMeasurementException extends RuntimeException{
	
	private final long serialVersionUID = 1L;
	public QuantityMeasurementException(String message) {
		super(message);	
	}

	public QuantityMeasurementException(String message, Throwable cause) {
		super(message, cause);
	}

}
