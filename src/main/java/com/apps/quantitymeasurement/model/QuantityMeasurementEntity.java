package com.apps.quantitymeasurement.model;
import java.time.LocalDateTime;
import com.apps.quantitymeasurement.core.IMeasurable;

/* public class QuantityMeasurementEntity implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	
	
	 public double thisValue;
	    public String thisUnit;
	    public String thisMeasurementType;

	    public double thatValue;
	    public String thatUnit;
	    public String thatMeasurementType;

	    public String operation;

	    public double resultValue;
	    public String resultUnit;
	    public String resultMeasurementType;
	    
	    public String resultString;

	    public boolean isError;
	    public String errorMessage;
	    
	    public QuantityMeasurementEntity(
	            QuantityModel<IMeasurable> thisQuantity,
	            QuantityModel<IMeasurable> thatQuantity,
	            String operation,
	            String result
	    ) {

	        this(thisQuantity, thatQuantity, operation);
	        this.resultString = result;
	    }


	    /**
	     * Arithmetic constructor
	     */
/*	    public QuantityMeasurementEntity(
	            QuantityModel<IMeasurable> thisQuantity,
	            QuantityModel<IMeasurable> thatQuantity,
	            String operation,
	            QuantityModel<IMeasurable> result
	    ) {

	        this(thisQuantity, thatQuantity, operation);

	        this.resultValue = result.value;
	        this.resultUnit = result.unit.getUnitName();
	        this.resultMeasurementType = result.unit.getMeasurementType();
	    }


	    /**
	     * Error constructor
	     */
	   /* public QuantityMeasurementEntity(
	            QuantityModel<IMeasurable> thisQuantity,
	            QuantityModel<IMeasurable> thatQuantity,
	            String operation,
	            String errorMessage,
	            boolean isError
	    ) {

	        this(thisQuantity, thatQuantity, operation);

	        this.errorMessage = errorMessage;
	        this.isError = isError;
	    }


	    /**
	     * Base constructor
	     */
	/*    private QuantityMeasurementEntity(
	            QuantityModel<IMeasurable> thisQuantity,
	            QuantityModel<IMeasurable> thatQuantity,
	            String operation
	    ) {

	        this.thisValue = thisQuantity.value;
	        this.thisUnit = thisQuantity.unit.getUnitName();
	        this.thisMeasurementType = thisQuantity.unit.getMeasurementType();

	        if (thatQuantity != null) {
	            this.thatValue = thatQuantity.value;
	            this.thatUnit = thatQuantity.unit.getUnitName();
	            this.thatMeasurementType = thatQuantity.unit.getMeasurementType();
	        }

	        this.operation = operation;
	    }
	*/



	    public class QuantityMeasurementEntity {

	        private int id;
	        private double value;
	        private String unit;
	        private String type;
	        private String operation;
	        private double result;
	        private LocalDateTime createdAt;

	        // ✅ Default Constructor
	        public QuantityMeasurementEntity() {
	        }

	        // ✅ Parameterized Constructor
	        public QuantityMeasurementEntity(double value, String unit, String type,
	                                         String operation, double result) {
	            this.value = value;
	            this.unit = unit;
	            this.type = type;
	            this.operation = operation;
	            this.result = result;
	            this.createdAt = LocalDateTime.now();
	        }

	        //  Getters and Setters

	        public int getId() {
	            return id;
	        }

	        public void setId(int id) {
	            this.id = id;
	        }


	        public double getValue() {
	            return value;
	        }

	        public void setValue(double value) {
	            this.value = value;
	        }


	        public String getUnit() {
	            return unit;
	        }

	        public void setUnit(String unit) {
	            this.unit = unit;
	        }


	        public String getType() {
	            return type;
	        }

	        public void setType(String type) {
	            this.type = type;
	        }


	        public String getOperation() {
	            return operation;
	        }

	        public void setOperation(String operation) {
	            this.operation = operation;
	        }


	        public double getResult() {
	            return result;
	        }

	        public void setResult(double result) {
	            this.result = result;
	        }


	        public LocalDateTime getCreatedAt() {
	            return createdAt;
	        }

	        public void setCreatedAt(LocalDateTime createdAt) {
	            this.createdAt = createdAt;
	        }


	        //  toString (useful for debugging)

	        @Override
	        public String toString() {
	            return "QuantityMeasurementEntity{" +
	                    "id=" + id +
	                    ", value=" + value +
	                    ", unit='" + unit + '\'' +
	                    ", type='" + type + '\'' +
	                    ", operation='" + operation + '\'' +
	                    ", result=" + result +
	                    ", createdAt=" + createdAt +
	                    '}';
	        }
	    }
	