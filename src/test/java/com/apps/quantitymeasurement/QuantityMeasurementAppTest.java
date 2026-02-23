package com.apps.quantitymeasurement;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QuantityMeasurementAppTest {

	@Test
	public void testEquality_FeetToFeet_SameValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(1.0, LengthUnit.FEET);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_InchToInch_SameValue() {
		Length l1 = new Length(1.0, LengthUnit.INCHES);
		Length l2 = new Length(1.0, LengthUnit.INCHES);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_FeetToInch_EquivalentValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);
		assertTrue(l1.equals(l2));
	}

	@Test
	public void testEquality_FeetToFeet_DifferentValue() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(2.0, LengthUnit.FEET);
		assertFalse(l1.equals(l2));
	}

	@Test
	public void testEquality_SameReference() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertTrue(l1.equals(l1));
	}

	@Test
	public void testEquality_NullComparison() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertFalse(l1.equals(null));
	}

	@Test
	public void testEquality_DifferentType() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		assertFalse(l1.equals("1.0"));
	}

	@Test
	public void testEquality_TransitiveProperty() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);
		Length l3 = new Length(1.0, LengthUnit.FEET);

		assertTrue(l1.equals(l2));
		assertTrue(l2.equals(l3));
		assertTrue(l1.equals(l3));
	}

	// Check if same yard values are equal
	@Test
	public void testEquality_YardToYard_SameValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check if different yard values are not equal
	@Test
	public void testEquality_YardToYard_DifferentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.YARDS)));
	}

	// Check yard to feet equivalent conversion (1 yard = 3 feet)
	@Test
	public void testEquality_YardToFeet_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(3.0, LengthUnit.FEET)));
	}

	// Check feet to yard equivalent conversion (symmetry)
	@Test
	public void testEquality_FeetToYard_EquivalentValue() {
		assertTrue(new Length(3.0, LengthUnit.FEET).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check yard to inches equivalent conversion (1 yard = 36 inches)
	@Test
	public void testEquality_YardToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.YARDS).equals(new Length(36.0, LengthUnit.INCHES)));
	}

	// Check inches to yard equivalent conversion
	@Test
	public void testEquality_InchesToYard_EquivalentValue() {
		assertTrue(new Length(36.0, LengthUnit.INCHES).equals(new Length(1.0, LengthUnit.YARDS)));
	}

	// Check yard to feet non-equivalent case
	@Test
	public void testEquality_YardToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.YARDS).equals(new Length(2.0, LengthUnit.FEET)));
	}

	// Check centimeter to inches equivalent conversion
	@Test
	public void testEquality_CentimetersToInches_EquivalentValue() {
		assertTrue(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(0.393701, LengthUnit.INCHES)));
	}

	// Check centimeter to feet non-equivalent case
	@Test
	public void testEquality_CentimetersToFeet_NonEquivalentValue() {
		assertFalse(new Length(1.0, LengthUnit.CENTIMETERS).equals(new Length(1.0, LengthUnit.FEET)));
	}

	// Check transitive property across multiple units
	@Test
	public void testEquality_MultiUnit_TransitiveProperty() {

		Length yard = new Length(1.0, LengthUnit.YARDS);
		Length feet = new Length(3.0, LengthUnit.FEET);
		Length inches = new Length(36.0, LengthUnit.INCHES);

		assertTrue(yard.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yard.equals(inches));
	}

	// Check complex equality across yards, feet and inches
	@Test
	public void testEquality_AllUnits_ComplexScenario() {

		Length yards = new Length(2.0, LengthUnit.YARDS);
		Length feet = new Length(6.0, LengthUnit.FEET);
		Length inches = new Length(72.0, LengthUnit.INCHES);

		assertTrue(yards.equals(feet));
		assertTrue(feet.equals(inches));
		assertTrue(yards.equals(inches));
	}

	// Check reflexive property for yard
	@Test
	public void testEquality_YardSameReference() {
		Length yard = new Length(1.0, LengthUnit.YARDS);
		assertTrue(yard.equals(yard));
	}

	// Check yard is not equal to null
	@Test
	public void testEquality_YardNullComparison() {
		Length yard = new Length(1.0, LengthUnit.YARDS);
		assertFalse(yard.equals(null));
	}

	// Check reflexive property for centimeter
	@Test
	public void testEquality_CentimetersSameReference() {
		Length cm = new Length(1.0, LengthUnit.CENTIMETERS);
		assertTrue(cm.equals(cm));
	}

	// Check centimeter is not equal to null
	@Test
	public void testEquality_CentimetersNullComparison() {
		Length cm = new Length(1.0, LengthUnit.CENTIMETERS);
		assertFalse(cm.equals(null));
	}

	private static final double EPSILON = 1e-6;

	@Test
	public void testConversion_FeetToInches() {
		assertEquals(12.0, Length.convert(1.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_InchesToFeet() {
		assertEquals(2.0, Length.convert(24.0, LengthUnit.INCHES, LengthUnit.FEET), EPSILON);
	}

	@Test
	public void testConversion_YardsToInches() {
		assertEquals(36.0, Length.convert(1.0, LengthUnit.YARDS, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_InchesToYards() {
		assertEquals(2.0, Length.convert(72.0, LengthUnit.INCHES, LengthUnit.YARDS), EPSILON);
	}

	@Test
	public void testConversion_CentimetersToInches() {
		assertEquals(1.0, Length.convert(2.54, LengthUnit.CENTIMETERS, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_FeetToYard() {
		assertEquals(2.0, Length.convert(6.0, LengthUnit.FEET, LengthUnit.YARDS), EPSILON);
	}

	@Test
	public void testConversion_RoundTrip_PreservesValue() {
		double original = 5.0;

		double toInches = Length.convert(original, LengthUnit.FEET, LengthUnit.INCHES);
		double backToFeet = Length.convert(toInches, LengthUnit.INCHES, LengthUnit.FEET);

		assertEquals(original, backToFeet, EPSILON);
	}

	@Test
	public void testConversion_ZeroValue() {
		assertEquals(0.0, Length.convert(0.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_NegativeValue() {
		assertEquals(-12.0, Length.convert(-1.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_SameUnit() {
		assertEquals(5.0, Length.convert(5.0, LengthUnit.FEET, LengthUnit.FEET), EPSILON);
	}

	@Test
	public void testConversion_InvalidUnit_Throws() {
		assertThrows(IllegalArgumentException.class, () -> Length.convert(1.0, null, LengthUnit.FEET));

		assertThrows(IllegalArgumentException.class, () -> Length.convert(1.0, LengthUnit.FEET, null));
	}

	@Test
	public void testConversion_NaNOrInfinite_Throws() {
		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.NaN, LengthUnit.FEET, LengthUnit.INCHES));

		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.POSITIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));

		assertThrows(IllegalArgumentException.class,
				() -> Length.convert(Double.NEGATIVE_INFINITY, LengthUnit.FEET, LengthUnit.INCHES));
	}

	@Test
	public void testConversion_LargeValue() {
		assertEquals(1200000.0, Length.convert(100000.0, LengthUnit.FEET, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testConversion_SmallValue() {
		assertEquals(0.393701, Length.convert(1.0, LengthUnit.CENTIMETERS, LengthUnit.INCHES), EPSILON);
	}

	@Test
	public void testAddition_SameUnit_FeetPlusFeet() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(2.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(3.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	public void testAddition_SameUnit_InchPlusInch() {
		Length l1 = new Length(6.0, LengthUnit.INCHES);
		Length l2 = new Length(6.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(12.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.INCHES, result.getUnit());
	}

	@Test
	public void testAddition_CrossUnit_FeetPlusInches() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(2.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	public void testAddition_CrossUnit_InchPlusFeet() {
		Length l1 = new Length(12.0, LengthUnit.INCHES);
		Length l2 = new Length(1.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(24.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.INCHES, result.getUnit());
	}

	@Test
	public void testAddition_CrossUnit_YardPlusFeet() {
		Length l1 = new Length(1.0, LengthUnit.YARDS);
		Length l2 = new Length(3.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(2.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.YARDS, result.getUnit());
	}

	@Test
	public void testAddition_CrossUnit_CentimeterPlusInch() {
		Length l1 = new Length(2.54, LengthUnit.CENTIMETERS);
		Length l2 = new Length(1.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(5.08, result.getValue(), EPSILON);
		assertEquals(LengthUnit.CENTIMETERS, result.getUnit());
	}

	@Test
	public void testAddition_Commutativity() {
		Length l1 = new Length(1.0, LengthUnit.FEET);
		Length l2 = new Length(12.0, LengthUnit.INCHES);

		Length result1 = l1.add(l2);
		Length result2 = l2.add(l1);

		assertTrue(result1.equals(result2));
	}

	@Test
	public void testAddition_WithZero() {
		Length l1 = new Length(5.0, LengthUnit.FEET);
		Length l2 = new Length(0.0, LengthUnit.INCHES);

		Length result = l1.add(l2);

		assertEquals(5.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	public void testAddition_NegativeValues() {
		Length l1 = new Length(5.0, LengthUnit.FEET);
		Length l2 = new Length(-2.0, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(3.0, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	public void testAddition_NullSecondOperand() {
		Length l1 = new Length(1.0, LengthUnit.FEET);

		assertThrows(IllegalArgumentException.class, () -> l1.add(null));
	}

	@Test
	public void testAddition_LargeValues() {
		Length l1 = new Length(1e6, LengthUnit.FEET);
		Length l2 = new Length(1e6, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(2e6, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}

	@Test
	public void testAddition_SmallValues() {
		Length l1 = new Length(0.001, LengthUnit.FEET);
		Length l2 = new Length(0.002, LengthUnit.FEET);

		Length result = l1.add(l2);

		assertEquals(0.003, result.getValue(), EPSILON);
		assertEquals(LengthUnit.FEET, result.getUnit());
	}
}}