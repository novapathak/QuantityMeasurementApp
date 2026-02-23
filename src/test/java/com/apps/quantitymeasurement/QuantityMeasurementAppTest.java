package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.apps.quantitymeasurement.LengthUnit;
	

	// Testing Class
	public class QuantityMeasurementAppTest {

	    // Feet target unit
	    @Test
	    void testAddition_ExplicitTargetUnit_Feet() {

	        Length result = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCH), LengthUnit.FEET);

	        assertEquals(new Length(2.0, LengthUnit.FEET), result);
	    }

	    // Inches target unit
	    @Test
	    void testAddition_ExplicitTargetUnit_Inches() {

	    	Length result = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCH), LengthUnit.INCH);

	        assertEquals(new Length(24.0, LengthUnit.INCH), result);
	    }

	    // Yards precision test
	    @Test
	    void testAddition_ExplicitTargetUnit_Yards() {

	    	Length result = new Length(1.0, LengthUnit.FEET).add(new Length(12.0, LengthUnit.INCH), LengthUnit.YARDS);

	    	Length expected = new Length((1.0 * LengthUnit.FEET.getConversionFactor()+ 12.0 * LengthUnit.INCH.getConversionFactor())/ LengthUnit.YARDS.getConversionFactor(),LengthUnit.YARDS);
            assertEquals(expected, result);
	    }

	    // Centimeter precision test
	    @Test
	    void testAddition_ExplicitTargetUnit_Centimeters() {

	    	Length result = new Length(1.0, LengthUnit.INCH)
	                .add(new Length(1.0, LengthUnit.INCH), LengthUnit.CENTIMETERS);

	    	Length expected = new Length(
	                (2.0 * LengthUnit.INCH.getConversionFactor())
	                        / LengthUnit.CENTIMETERS.getConversionFactor(),
	                LengthUnit.CENTIMETERS);

	        assertEquals(expected, result);
	    }

	    // Same as first operand unit
	    @Test
	    void testAddition_ExplicitTargetUnit_SameAsFirstOperand() {

	    	Length result = new Length(2.0, LengthUnit.YARDS)
	                .add(new Length(3.0, LengthUnit.FEET), LengthUnit.YARDS);

	        assertEquals(new Length(3.0, LengthUnit.YARDS), result);
	    }

	    // Same as second operand unit
	    @Test
	    void testAddition_ExplicitTargetUnit_SameAsSecondOperand() {

	    	Length result = new Length(2.0, LengthUnit.YARDS)
	                .add(new Length(3.0, LengthUnit.FEET), LengthUnit.FEET);

	        assertEquals(new Length(9.0, LengthUnit.FEET), result);
	    }

	    // Commutativity test
	    @Test
	    void testAddition_ExplicitTargetUnit_Commutativity() {

	    	Length result1 = new Length(1.0, LengthUnit.FEET)
	                .add(new Length(12.0, LengthUnit.INCH), LengthUnit.YARDS);

	    	Length result2 = new Length(12.0, LengthUnit.INCH)
	                .add(new Length(1.0, LengthUnit.FEET), LengthUnit.YARDS);

	        assertEquals(result1, result2);
	    }

	    // Zero operand test
	    @Test
	    void testAddition_ExplicitTargetUnit_WithZero() {

	    	Length result = new Length(5.0, LengthUnit.FEET)
	                .add(new Length(0.0, LengthUnit.INCH), LengthUnit.YARDS);

	    	Length expected = new Length(
	                (5.0 * LengthUnit.FEET.getConversionFactor())
	                        / LengthUnit.YARDS.getConversionFactor(),
	                LengthUnit.YARDS);

	        assertEquals(expected, result);
	    }

	    // Negative values test
	    @Test
	    void testAddition_ExplicitTargetUnit_NegativeValues() {

	    	Length result = new Length(5.0, LengthUnit.FEET)
	                .add(new Length(-2.0, LengthUnit.FEET), LengthUnit.INCH);

	    	Length expected = new Length(
	                (5.0 * LengthUnit.FEET.getConversionFactor()
	                        - 2.0 * LengthUnit.FEET.getConversionFactor())
	                        / LengthUnit.INCH.getConversionFactor(),
	                LengthUnit.INCH);

	        assertEquals(expected, result);
	    }

	    // Null target unit test
	    @Test
	    void testAddition_ExplicitTargetUnit_NullTargetUnit() {

	        assertThrows(IllegalArgumentException.class, () ->
	                new Length(1.0, LengthUnit.FEET)
	                        .add(new Length(12.0, LengthUnit.INCH), null));
	    }

	}